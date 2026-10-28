package ru.dverkask.polyglotmc.provider;

import ru.dverkask.polyglotmc.api.TranslationProvider;
import ru.dverkask.polyglotmc.lang.SupportedLanguage;
import ru.dverkask.polyglotmc.loader.TranslationManager;
import ru.dverkask.polyglotmc.version.Version;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

/**
 * A caching implementation of {@link TranslationProvider}.
 * <p>
 * This class uses {@link TranslationManager} to load and cache the full language file for a specific
 * language and game version. It provides fast access to translations by storing them in memory after initial loading.
 * </p>
 * <p>
 * If the initial load fails, lookups return nothing and the load is retried in the background
 * (at most once per {@value TranslationManager#RETRY_DELAY_SECONDS} seconds) until it succeeds.
 * </p>
 *
 * @author DverkaSK
 * @version 2.1
 * @since 1.0
 */
public class CacheTranslationProvider implements TranslationProvider {

    private final SupportedLanguage language;
    private final Version version;
    private final Path cacheDirectory;

    /**
     * A map containing cached translations.
     * The key is the translation key and the value is its translated string. Empty until loaded.
     */
    private volatile Map<String, String> translations;

    /**
     * Constructs a new CacheTranslationProvider for the specified language and game version.
     * Blocks until the language file is loaded.
     *
     * @param language The {@link SupportedLanguage} for which to provide translations
     * @param version The {@link Version} of Minecraft for which to provide translations
     */
    public CacheTranslationProvider(SupportedLanguage language, Version version) {
        this(language, version, null);
    }

    /**
     * Constructs a new CacheTranslationProvider that caches language files on disk.
     * Blocks until the language file is loaded.
     *
     * @param language The {@link SupportedLanguage} for which to provide translations
     * @param version The {@link Version} of Minecraft for which to provide translations
     * @param cacheDirectory The directory to cache language files in, or {@code null} to always download them
     * @since 2.1
     */
    public CacheTranslationProvider(SupportedLanguage language, Version version, Path cacheDirectory) {
        this.language = language;
        this.version = version;
        this.cacheDirectory = cacheDirectory;
        this.translations = TranslationManager.getInstance().getTranslations(language, version, cacheDirectory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getTranslation(String key) {
        Map<String, String> current = translations;
        if (current.isEmpty()) {
            current = TranslationManager.getInstance().request(language, version, cacheDirectory).orElse(current);
            translations = current;
        }
        return Optional.ofNullable(current.get(key));
    }
}
