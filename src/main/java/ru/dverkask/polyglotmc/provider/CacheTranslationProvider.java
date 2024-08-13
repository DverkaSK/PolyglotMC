package ru.dverkask.polyglotmc.provider;

import org.bukkit.Material;
import ru.dverkask.polyglotmc.api.TranslationProvider;
import ru.dverkask.polyglotmc.lang.SupportedLanguage;
import ru.dverkask.polyglotmc.loader.TranslationManager;
import ru.dverkask.polyglotmc.version.Version;

import java.util.Map;
import java.util.Optional;

public class CacheTranslationProvider implements TranslationProvider {
    private final Map<Material, String> translations;

    public CacheTranslationProvider(SupportedLanguage language, Version version) {
        this.translations = TranslationManager.getInstance().getTranslations(language, version);
    }

    @Override
    public Optional<String> getTranslation(Material item) {
        return Optional.ofNullable(translations.get(item));
    }
}
