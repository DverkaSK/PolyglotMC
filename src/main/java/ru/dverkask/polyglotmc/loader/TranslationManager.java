package ru.dverkask.polyglotmc.loader;

import ru.dverkask.polyglotmc.lang.SupportedLanguage;
import ru.dverkask.polyglotmc.version.Version;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages translations for Minecraft across different languages and versions.
 * <p>
 * This class implements the Singleton pattern to ensure a single instance of translation management
 * throughout the application. Loaded language files are cached per language and version,
 * so several {@link ru.dverkask.polyglotmc.api.Polyglot} instances share the same data.
 * Failed loads are not cached and will be retried on the next request.
 * </p>
 *
 * @author DverkaSK
 * @version 2.0
 * @since 1.0
 */
public final class TranslationManager {

    private static final Logger LOGGER = Logger.getLogger("PolyglotMC");

    /** The single instance of TranslationManager. */
    private static final TranslationManager INSTANCE = new TranslationManager();

    /**
     * A map storing translations for each language and version pair.
     * The outer map uses {@code "<version>/<language>"} as keys and the inner map uses translation keys as keys.
     */
    private final Map<String, Map<String, String>> translations = new ConcurrentHashMap<>();

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
        String cacheKey = version.version() + "/" + language.getCode();
        Map<String, String> cached = translations.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        try {
            Map<String, String> loaded = Map.copyOf(new TranslationLoader(language, version).loadTranslations());
            Map<String, String> previous = translations.putIfAbsent(cacheKey, loaded);
            return previous != null ? previous : loaded;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to load translations for " + cacheKey, e);
            return Map.of();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Map.of();
        }
    }
}
