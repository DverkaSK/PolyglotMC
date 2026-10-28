package ru.dverkask.polyglotmc.loader;

import ru.dverkask.polyglotmc.lang.SupportedLanguage;
import ru.dverkask.polyglotmc.version.Version;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages translations for Minecraft across different languages and versions.
 * <p>
 * This class implements the Singleton pattern to ensure a single instance of translation management
 * throughout the application. Loaded language files are cached per language and version,
 * so several {@link ru.dverkask.polyglotmc.api.Polyglot} instances share the same data.
 * </p>
 * <p>
 * Each language file is downloaded at most once at a time: concurrent requests wait for the same download.
 * Failed loads are not cached; after a failure the next attempt is made no sooner than
 * {@value #RETRY_DELAY_SECONDS} seconds later, so a missing network does not turn every lookup into a request.
 * </p>
 *
 * @author DverkaSK
 * @version 2.1
 * @since 1.0
 */
public final class TranslationManager {

    /** Seconds to wait after a failed load before the same language file is requested again. */
    public static final long RETRY_DELAY_SECONDS = 60;

    private static final Logger LOGGER = Logger.getLogger("PolyglotMC");

    /** The single instance of TranslationManager. */
    private static final TranslationManager INSTANCE = new TranslationManager();

    /** Runs downloads, in daemon threads so a pending download never keeps the JVM alive. */
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(task -> {
        Thread thread = new Thread(task, "PolyglotMC-loader");
        thread.setDaemon(true);
        return thread;
    });

    /**
     * A map storing translations for each language and version pair.
     * The outer map uses {@code "<version>/<language>"} as keys and the inner map uses translation keys as keys.
     */
    private final Map<String, Map<String, String>> translations = new ConcurrentHashMap<>();

    /** Loads in progress, by the same keys as {@link #translations}. */
    private final Map<String, CompletableFuture<Map<String, String>>> pending = new ConcurrentHashMap<>();

    /** {@link System#nanoTime()} of the last failed load, by the same keys as {@link #translations}. */
    private final Map<String, Long> failedAt = new ConcurrentHashMap<>();

    /**
     * Private constructor to prevent direct instantiation.
     */
    private TranslationManager() {
    }

    /**
     * Returns the single instance of TranslationManager.
     *
     * @return The singleton instance of {@link TranslationManager}
     */
    public static TranslationManager getInstance() {
        return INSTANCE;
    }

    /**
     * Retrieves translations for a specific language and Minecraft version.
     * If translations are not cached, they will be loaded.
     *
     * @param language The {@link SupportedLanguage} for which to retrieve translations
     * @param version The {@link Version} of Minecraft for which to retrieve translations
     * @return A {@link Map} where keys are translation keys and values are their translated strings.
     *         Returns an empty map if loading failed.
     */
    public Map<String, String> getTranslations(SupportedLanguage language, Version version) {
        return getTranslations(language, version, null);
    }

    /**
     * Retrieves translations for a specific language and Minecraft version, caching language files on disk.
     * If translations are not cached, they will be loaded; this call blocks until then.
     *
     * @param language The {@link SupportedLanguage} for which to retrieve translations
     * @param version The {@link Version} of Minecraft for which to retrieve translations
     * @param cacheDirectory The directory to cache language files in, or {@code null} to always download them
     * @return A {@link Map} where keys are translation keys and values are their translated strings.
     *         Returns an empty map if loading failed.
     * @since 2.1
     */
    public Map<String, String> getTranslations(SupportedLanguage language, Version version, Path cacheDirectory) {
        try {
            return load(language, version, cacheDirectory);
        } catch (IOException e) {
            return Map.of();
        }
    }

    /**
     * Loads translations for a specific language and Minecraft version, blocking until they are available.
     *
     * @param language The {@link SupportedLanguage} for which to load translations
     * @param version The {@link Version} of Minecraft for which to load translations
     * @param cacheDirectory The directory to cache language files in, or {@code null} to always download them
     * @return A {@link Map} where keys are translation keys and values are their translated strings
     * @throws IOException if the language file could not be loaded, now or in an attempt
     *                     less than {@value #RETRY_DELAY_SECONDS} seconds ago
     * @since 2.1
     */
    public Map<String, String> load(SupportedLanguage language, Version version, Path cacheDirectory) throws IOException {
        try {
            return fetch(language, version, cacheDirectory).get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof UncheckedIOException unchecked) {
                throw unchecked.getCause();
            }
            throw new IOException(cause);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException("Interrupted while loading " + cacheKey(language, version));
        }
    }

    /**
     * Returns cached translations without blocking. If they are not cached, a load is started
     * in the background (unless one is running or the last attempt failed recently).
     *
     * @param language The {@link SupportedLanguage} for which to retrieve translations
     * @param version The {@link Version} of Minecraft for which to retrieve translations
     * @param cacheDirectory The directory to cache language files in, or {@code null} to always download them
     * @return The translations if they are already loaded, otherwise an empty {@link Optional}
     * @since 2.1
     */
    public Optional<Map<String, String>> request(SupportedLanguage language, Version version, Path cacheDirectory) {
        CompletableFuture<Map<String, String>> future = fetch(language, version, cacheDirectory);
        return future.isDone() && !future.isCompletedExceptionally()
                ? Optional.of(future.join())
                : Optional.empty();
    }

    private CompletableFuture<Map<String, String>> fetch(SupportedLanguage language, Version version, Path cacheDirectory) {
        String key = cacheKey(language, version);
        Map<String, String> cached = translations.get(key);
        if (cached != null) {
            return CompletableFuture.completedFuture(cached);
        }
        Long failed = failedAt.get(key);
        if (failed != null && System.nanoTime() - failed < TimeUnit.SECONDS.toNanos(RETRY_DELAY_SECONDS)) {
            return CompletableFuture.failedFuture(new UncheckedIOException(new IOException(
                    "Loading " + key + " failed less than " + RETRY_DELAY_SECONDS + " seconds ago")));
        }

        CompletableFuture<Map<String, String>> created = new CompletableFuture<>();
        CompletableFuture<Map<String, String>> running = pending.putIfAbsent(key, created);
        if (running != null) {
            return running;
        }
        EXECUTOR.execute(() -> {
            try {
                Map<String, String> loaded = translations.get(key);
                if (loaded == null) {
                    loaded = Map.copyOf(new TranslationLoader(language, version, cacheDirectory).loadTranslations());
                    translations.put(key, loaded);
                    failedAt.remove(key);
                }
                created.complete(loaded);
            } catch (IOException e) {
                failedAt.put(key, System.nanoTime());
                LOGGER.log(Level.WARNING, "Failed to load translations for " + key, e);
                created.completeExceptionally(new UncheckedIOException(e));
            } catch (InterruptedException e) {
                created.completeExceptionally(new UncheckedIOException(new InterruptedIOException(key)));
            } catch (RuntimeException e) {
                failedAt.put(key, System.nanoTime());
                LOGGER.log(Level.WARNING, "Failed to load translations for " + key, e);
                created.completeExceptionally(e);
            } finally {
                pending.remove(key, created);
            }
        });
        return created;
    }

    private static String cacheKey(SupportedLanguage language, Version version) {
        return version.version() + "/" + language.getCode();
    }
}
