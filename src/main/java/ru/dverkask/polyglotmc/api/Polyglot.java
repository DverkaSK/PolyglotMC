package ru.dverkask.polyglotmc.api;

import net.kyori.adventure.translation.Translatable;
import ru.dverkask.polyglotmc.lang.SupportedLanguage;
import ru.dverkask.polyglotmc.provider.CacheTranslationProvider;
import ru.dverkask.polyglotmc.version.SupportedVersion;
import ru.dverkask.polyglotmc.version.Version;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * The main class of the PolyglotMC library, providing server-side translation of Minecraft names
 * into different languages.
 * <p>
 * Anything that implements Adventure's {@link Translatable} can be translated: materials, item stacks,
 * entity types, enchantments, potion effect types, biomes, attributes, game rules and more.
 * Raw translation keys (e.g. {@code death.attack.fall}) are supported as well.
 * </p>
 * <p>
 * This class uses the Builder pattern for configuration and instantiation. It manages a collection of
 * {@link TranslationProvider}s for different languages.
 * </p>
 *
 * @author DverkaSK
 * @version 2.0
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
        this.translationProviders = Collections.synchronizedMap(new EnumMap<>(builder.translationProviders));
        this.defaultLanguage = builder.defaultLanguage;
        this.dynamicLanguageLoading = builder.dynamicLanguageLoading;
        this.version = builder.version;
    }

    /**
     * Translates a translation key to the specified language.
     * <p>
     * If the key is missing in the requested language, the translation in the default language is returned.
     * If it is missing there too, the key itself is returned.
     * </p>
     *
     * @param key      The translation key, e.g. {@code item.minecraft.diamond_sword}.
     * @param language The target language for translation.
     * @return A {@link TranslationResult} containing the key and the translated string.
     * @throws NullPointerException if the key or language is null.
     */
    public TranslationResult translate(String key, SupportedLanguage language) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(language, "language");

        Optional<String> translation = getProvider(language)
                .flatMap(provider -> provider.getTranslation(key));
        if (translation.isPresent()) {
            return new TranslationResult(key, translation.get(), language, true);
        }

        String fallback = language == defaultLanguage
                ? key
                : getProvider(defaultLanguage)
                        .flatMap(provider -> provider.getTranslation(key))
                        .orElse(key);
        return new TranslationResult(key, fallback, language, false);
    }

    /**
     * Translates a translation key to the default language.
     *
     * @param key The translation key, e.g. {@code item.minecraft.diamond_sword}.
     * @return A {@link TranslationResult} containing the key and the translated string.
     * @throws NullPointerException if the key is null.
     */
    public TranslationResult translate(String key) {
        return translate(key, defaultLanguage);
    }

    /**
     * Translates the name of any translatable game object to the specified language.
     * <p>
     * Examples of supported objects: {@link org.bukkit.Material}, {@link org.bukkit.inventory.ItemStack},
     * {@link org.bukkit.entity.EntityType}, {@link org.bukkit.enchantments.Enchantment},
     * {@link org.bukkit.potion.PotionEffectType}, {@link org.bukkit.block.Biome},
     * {@link org.bukkit.attribute.Attribute}, {@link org.bukkit.GameRule}.
     * </p>
     *
     * @param translatable The object to translate.
     * @param language     The target language for translation.
     * @return A {@link TranslationResult} containing the key and the translated string.
     * @throws NullPointerException if the object or language is null.
     */
    public TranslationResult translate(Translatable translatable, SupportedLanguage language) {
        Objects.requireNonNull(translatable, "translatable");
        return translate(translatable.translationKey(), language);
    }

    /**
     * Translates the name of any translatable game object to the default language.
     *
     * @param translatable The object to translate.
     * @return A {@link TranslationResult} containing the key and the translated string.
     * @throws NullPointerException if the object is null.
     */
    public TranslationResult translate(Translatable translatable) {
        return translate(translatable, defaultLanguage);
    }

    /**
     * Returns the Minecraft version this instance provides translations for.
     *
     * @return The configured {@link Version}.
     */
    public Version getVersion() {
        return version;
    }

    /**
     * Returns the default language of this instance.
     *
     * @return The default {@link SupportedLanguage}.
     */
    public SupportedLanguage getDefaultLanguage() {
        return defaultLanguage;
    }

    private Optional<TranslationProvider> getProvider(SupportedLanguage language) {
        if (dynamicLanguageLoading) {
            return Optional.of(translationProviders.computeIfAbsent(language,
                    lang -> new CacheTranslationProvider(lang, version)));
        }
        return Optional.ofNullable(translationProviders.get(language));
    }

    /**
     * Builder class for creating instances of {@link Polyglot}.
     * <p>
     * This class allows for fluent configuration of the Polyglot instance.
     * </p>
     */
    public static class Builder {

        private final Map<SupportedLanguage, TranslationProvider> translationProviders = new EnumMap<>(SupportedLanguage.class);
        private final Set<SupportedLanguage> languages = EnumSet.noneOf(SupportedLanguage.class);
        private SupportedLanguage defaultLanguage = SupportedLanguage.EN_US;
        private Version version = SupportedVersion.Release.V1_20_4;
        private boolean dynamicLanguageLoading = false;

        /**
         * Sets the Minecraft version for translations.
         * <p>
         * If not set, the default version is {@link SupportedVersion.Release#V1_20_4}.
         * Versions that are not listed in {@link SupportedVersion} can be created with {@link Version#of(String)}.
         * </p>
         *
         * @param version The Minecraft version to use.
         * @return This Builder instance.
         */
        public Builder withVersion(Version version) {
            this.version = Objects.requireNonNull(version, "version");
            return this;
        }

        /**
         * Adds supported languages to the Polyglot instance.
         * <p>
         * By default, no languages are added. You must specify at least one language.
         * </p>
         * <p>
         * For each language, a {@link CacheTranslationProvider} is used as the default translation provider.
         * Providers are created in {@link #build()}, so the call order relative to {@link #withVersion(Version)}
         * does not matter.
         * </p>
         *
         * @param languages The languages to add.
         * @return This Builder instance.
         */
        public Builder withLanguages(SupportedLanguage... languages) {
            this.languages.addAll(Arrays.asList(languages));
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
            this.defaultLanguage = Objects.requireNonNull(language, "language");
            return this;
        }

        /**
         * Enables or disables dynamic language loading.
         * <p>
         * When enabled, Polyglot will load translations for languages
         * that were not initially specified on first use.
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
         * <p>
         * Language files of all specified languages are downloaded here, so this call may block.
         * Consider calling it asynchronously.
         * </p>
         *
         * @return A new Polyglot instance configured with this builder.
         * @throws IllegalStateException if no languages are specified or if the default language is not included.
         */
        public Polyglot build() {
            for (SupportedLanguage language : languages) {
                translationProviders.computeIfAbsent(language, lang -> new CacheTranslationProvider(lang, version));
            }
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
