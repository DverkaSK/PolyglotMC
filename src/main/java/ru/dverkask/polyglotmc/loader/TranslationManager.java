package ru.dverkask.polyglotmc.loader;

import org.bukkit.Material;
import ru.dverkask.polyglotmc.lang.SupportedLanguage;
import ru.dverkask.polyglotmc.version.Version;

import java.util.EnumMap;
import java.util.Map;

public final class TranslationManager {

    private volatile static TranslationManager instance;
    private final Map<SupportedLanguage, Map<Material, String>> translations;

    private TranslationManager() {
        this.translations = new EnumMap<>(SupportedLanguage.class);
    }

    public static TranslationManager getInstance() {
        if (instance == null) {
            instance = new TranslationManager();
        }
        return instance;
    }

    public Map<Material, String> getTranslations(SupportedLanguage language, Version version) {
        return translations.computeIfAbsent(language, lang -> loadTranslations(lang, version));
    }

    private Map<Material, String> loadTranslations(SupportedLanguage language, Version version) {
        TranslationLoader loader = new TranslationLoader(language, version);
        return loader.loadTranslations();
    }
}
