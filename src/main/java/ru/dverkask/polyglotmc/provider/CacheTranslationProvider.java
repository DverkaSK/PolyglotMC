package ru.dverkask.polyglotmc.provider;

import org.bukkit.Material;
import ru.dverkask.polyglotmc.api.TranslationProvider;
import ru.dverkask.polyglotmc.lang.SupportedLanguage;
import ru.dverkask.polyglotmc.loader.TranslationManager;
import ru.dverkask.polyglotmc.version.Version;

import java.util.Map;
import java.util.Optional;

/**
 * A caching implementation of {@link TranslationProvider} that provides translations for Minecraft materials.
 * <p>
 * This class uses {@link TranslationManager} to load and cache translations for a specific language and game version.
 * It provides fast access to translations by storing them in memory after initial loading.
 * </p>
 *
 * @author DverkaSK
 * @version 1.0
 * @since 1.0
 */
public class CacheTranslationProvider implements TranslationProvider {

    /**
     * A map containing cached translations for Minecraft materials.
     * The key is the {@link Material} and the value is its translated name.
     */
    private final Map<Material, String> translations;

    /**
     * Constructs a new CacheTranslationProvider for the specified language and game version.
     *
     * @param language The {@link SupportedLanguage} for which to provide translations
     * @param version The {@link Version} of Minecraft for which to provide translations
     * @throws IllegalArgumentException if the language or version is not supported
     */
    public CacheTranslationProvider(SupportedLanguage language, Version version) {
        this.translations = TranslationManager.getInstance().getTranslations(language, version);
    }

    /**
     * Retrieves the translation for the specified Minecraft material.
     *
     * @param item The {@link Material} for which to get the translation
     * @return An {@link Optional} containing the translated name of the material,
     *         or an empty Optional if no translation is available
     */
    @Override
    public Optional<String> getTranslation(Material item) {
        return Optional.ofNullable(translations.get(item));
    }
}
