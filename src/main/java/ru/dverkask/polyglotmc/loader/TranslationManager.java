package ru.dverkask.polyglotmc.loader;

import org.bukkit.Material;
import ru.dverkask.polyglotmc.lang.SupportedLanguage;
import ru.dverkask.polyglotmc.version.Version;

import java.util.EnumMap;
import java.util.Map;

/**
 * Manages translations for Minecraft items and blocks across different languages and versions.
 * <p>
 * This class implements the Singleton pattern to ensure a single instance of translation management
 * throughout the application. It provides methods to retrieve translations for specific languages
 * and Minecraft versions, caching results for improved performance.
 * </p>
 *
 * @author DverkaSK
 * @version 1.0
 * @since 1.0
 */
public final class TranslationManager {

    /** The single instance of TranslationManager. */
    private volatile static TranslationManager instance;

    /**
     * A map storing translations for each supported language.
     * The outer map uses {@link SupportedLanguage} as keys and the inner map uses {@link Material} as keys.
     */
    private final Map<SupportedLanguage, Map<Material, String>> translations;

    /**
     * Private constructor to prevent direct instantiation.
     * Initializes the translations map.
     */
    private TranslationManager() {
        this.translations = new EnumMap<>(SupportedLanguage.class);
    }

    /**
     * Returns the single instance of TranslationManager.
     * Creates a new instance if one doesn't exist.
     *
     * @return The singleton instance of {@link TranslationManager}
     */
    public static TranslationManager getInstance() {
        if (instance == null) {
            instance = new TranslationManager();
        }
        return instance;
    }

    /**
     * Retrieves translations for a specific language and Minecraft version.
     * If translations for the given language are not cached, they will be loaded.
     *
     * @param language The {@link SupportedLanguage} for which to retrieve translations
     * @param version The {@link Version} of Minecraft for which to retrieve translations
     * @return A {@link Map} where keys are {@link Material} objects and values are their translated names
     */
    public Map<Material, String> getTranslations(SupportedLanguage language, Version version) {
        return translations.computeIfAbsent(language, lang -> loadTranslations(lang, version));
    }

    /**
     * Loads translations for a specific language and Minecraft version.
     * This method is called internally when translations for a language are not yet cached.
     *
     * @param language The {@link SupportedLanguage} for which to load translations
     * @param version The {@link Version} of Minecraft for which to load translations
     * @return A {@link Map} where keys are {@link Material} objects and values are their translated names
     */
    private Map<Material, String> loadTranslations(SupportedLanguage language, Version version) {
        TranslationLoader loader = new TranslationLoader(language, version);
        return loader.loadTranslations();
    }
}
