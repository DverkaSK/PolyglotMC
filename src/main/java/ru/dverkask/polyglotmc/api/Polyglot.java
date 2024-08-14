package ru.dverkask.polyglotmc.api;

import org.bukkit.Material;
import ru.dverkask.polyglotmc.lang.SupportedLanguage;
import ru.dverkask.polyglotmc.provider.CacheTranslationProvider;
import ru.dverkask.polyglotmc.version.SupportedVersion;
import ru.dverkask.polyglotmc.version.Version;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * The main class of the PolyglotMC library, providing functionality for translating Minecraft item names
 * into different languages.
 * <p>
 * This class uses the Builder pattern for configuration and instantiation. It manages a collection of
 * {@link TranslationProvider}s for different languages and provides methods to translate item names.
 * </p>
 *
 * @author DverkaSK
 * @version 1.0
 * @since 1.0
 */
public final class Polyglot {

    /**
     * A map of translation providers for each supported language.
     */
    private final Map<SupportedLanguage, TranslationProvider> translationProviders;

    /**
     * The default language to use when a specific language is not available.
     */
    private final SupportedLanguage defaultLanguage;

    /**
     * Flag indicating whether dynamic language loading is enabled.
     */
    private final boolean dynamicLanguageLoading;

    /**
     * The Minecraft version for which translations are provided.
     */
    private final Version version;

    private Polyglot(Builder builder) {
        this.translationProviders = builder.translationProviders;
        this.defaultLanguage = builder.defaultLanguage;
        this.dynamicLanguageLoading = builder.dynamicLanguageLoading;
        this.version = builder.version;
    }

    /**
     * Translates the name of a Minecraft item to the specified language.
     *
     * @param item     The Minecraft item to translate.
     * @param language The target language for translation.
     * @return A {@link TranslationResult} containing the original and translated names.
     * @throws IllegalArgumentException if the item or language is null.
     */
    public TranslationResult translate(Material item, SupportedLanguage language) {
        TranslationProvider provider = translationProviders.get(language);
        if (provider == null) {
            if (dynamicLanguageLoading) {
                provider = new CacheTranslationProvider(language, version);
                translationProviders.put(language, provider);
            } else {
                provider = translationProviders.get(defaultLanguage);
            }
        }

        Optional<String> translation = provider.getTranslation(item);
        return new TranslationResult(
                item.name(),
                translation.orElse(item.name()),
                language,
                translation.isPresent()
        );
    }

    /**
     * Translates the name of a Minecraft item to the default language.
     *
     * @param item The Minecraft item to translate.
     * @return A {@link TranslationResult} containing the original and translated names.
     * @throws IllegalArgumentException if the item is null.
     */
    public TranslationResult translate(Material item) {
        return translate(item, defaultLanguage);
    }

    /**
     * Builder class for creating instances of {@link Polyglot}.
     * <p>
     * This class allows for fluent configuration of the Polyglot instance.
     * </p>
     */
    public static class Builder {

        private final Map<SupportedLanguage, TranslationProvider> translationProviders = new EnumMap<>(SupportedLanguage.class);
        private SupportedLanguage defaultLanguage = SupportedLanguage.EN_US;
        private Version version = SupportedVersion.Release.V1_20_4;
        private boolean dynamicLanguageLoading = false;

        /**
         * Sets the Minecraft version for translations.
         * <p>
         * If not set, the default version is {@link SupportedVersion.Release#V1_20_4}.
         * </p>
         *
         * @param version The Minecraft version to use.
         * @return This Builder instance.
         */
        public Builder withVersion(Version version) {
            this.version = version;
            return this;
        }

        /**
         * Adds supported languages to the Polyglot instance.
         * <p>
         * By default, no languages are added. You must specify at least one language.
         * </p>
         * <p>
         * For each language, a {@link CacheTranslationProvider} is used as the default translation provider.
         * </p>
         *
         * @param languages The languages to add.
         * @return This Builder instance.
         */
        public Builder withLanguages(SupportedLanguage... languages) {
            for (SupportedLanguage language : languages) {
                this.translationProviders.put(language, new CacheTranslationProvider(language, version));
            }
            return this;
        }

        /**
         * Adds a custom translation provider for a specific language.
         * <p>
         * This method allows you to override the default {@link CacheTranslationProvider}
         * with a custom implementation for specific languages.
         * </p>
         *
         * @param language The language for the provider.
         * @param provider The custom translation provider.
         * @return This Builder instance.
         */
        public Builder withTranslationProvider(SupportedLanguage language, TranslationProvider provider) {
            this.translationProviders.put(language, provider);
            return this;
        }

        /**
         * Sets the default language for translations.
         * <p>
         * If not set, the default language is {@link SupportedLanguage#EN_US}.
         * </p>
         *
         * @param language The default language to use.
         * @return This Builder instance.
         */
        public Builder withDefaultLanguage(SupportedLanguage language) {
            this.defaultLanguage = language;
            return this;
        }

        /**
         * Enables or disables dynamic language loading.
         * <p>
         * When enabled, Polyglot will attempt to load translations for languages
         * that were not initially specified. This can be useful for supporting
         * a wide range of languages without explicitly configuring each one.
         * </p>
         * <p>
         * By default, dynamic language loading is disabled.
         * </p>
         *
         * @param dynamicLanguageLoading true to enable dynamic language loading, false to disable it.
         * @return This Builder instance.
         */
        public Builder withDynamicLanguageLoading(boolean dynamicLanguageLoading) {
            this.dynamicLanguageLoading = dynamicLanguageLoading;
            return this;
        }

        /**
         * Builds and returns a new Polyglot instance.
         *
         * @return A new Polyglot instance configured with this builder.
         * @throws IllegalStateException if no languages are specified or if the default language is not included.
         */
        public Polyglot build() {
            if (translationProviders.isEmpty()) {
                throw new IllegalStateException("At least one language must be specified");
            }
            if (!translationProviders.containsKey(defaultLanguage)) {
                throw new IllegalStateException("Default language must be included in specified languages");
            }
            return new Polyglot(this);
        }
    }
}
