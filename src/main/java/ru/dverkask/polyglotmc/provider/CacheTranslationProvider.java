package ru.dverkask.polyglotmc.provider;

import ru.dverkask.polyglotmc.api.TranslationProvider;
import ru.dverkask.polyglotmc.lang.SupportedLanguage;
import ru.dverkask.polyglotmc.loader.TranslationManager;
import ru.dverkask.polyglotmc.version.Version;

import java.util.Map;
import java.util.Optional;

/**
 * A caching implementation of {@link TranslationProvider}.
 * <p>
 * This class uses {@link TranslationManager} to load and cache the full language file for a specific
 * language and game version. It provides fast access to translations by storing them in memory after initial loading.
 * </p>
 *
 * @author DverkaSK
 * @version 2.0
 * @since 1.0
 */
public class CacheTranslationProvider implements TranslationProvider {

    /**
     * A map containing cached translations.
     * The key is the translation key and the value is its translated string.
     */
    private final Map<String, String> translations;

    /**
     * Constructs a new CacheTranslationProvider for the specified language and game version.
     *
     * @param language The {@link SupportedLanguage} for which to provide translations
     * @param version The {@link Version} of Minecraft for which to provide translations
     */
    public CacheTranslationProvider(SupportedLanguage language, Version version) {
        this.translations = TranslationManager.getInstance().getTranslations(language, version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getTranslation(String key) {
        return Optional.ofNullable(translations.get(key));
    }
}
