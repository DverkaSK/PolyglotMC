package ru.dverkask.polyglotmc.api;

import org.bukkit.Material;
import ru.dverkask.polyglotmc.lang.SupportedLanguage;
import ru.dverkask.polyglotmc.provider.CacheTranslationProvider;
import ru.dverkask.polyglotmc.version.SupportedVersion;
import ru.dverkask.polyglotmc.version.Version;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public final class Polyglot {

    private final Map<SupportedLanguage, TranslationProvider> translationProviders;
    private final SupportedLanguage defaultLanguage;
    private final boolean dynamicLanguageLoading;
    private final Version version;

    private Polyglot(Builder builder) {
        this.translationProviders = builder.translationProviders;
        this.defaultLanguage = builder.defaultLanguage;
        this.dynamicLanguageLoading = builder.dynamicLanguageLoading;
        this.version = builder.version;
    }

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

    public TranslationResult translate(Material item) {
        return translate(item, defaultLanguage);
    }

    public static class Builder {

        private final Map<SupportedLanguage, TranslationProvider> translationProviders = new EnumMap<>(SupportedLanguage.class);
        private SupportedLanguage defaultLanguage = SupportedLanguage.EN_US;
        private Version version = SupportedVersion.Release.V1_20_4;
        private boolean dynamicLanguageLoading = false;

        public Builder withVersion(Version version) {
            this.version = version;
            return this;
        }

        public Builder withLanguages(SupportedLanguage... languages) {
            for (SupportedLanguage language : languages) {
                this.translationProviders.put(language, new CacheTranslationProvider(language, version));
            }
            return this;
        }

        public Builder withTranslationProvider(SupportedLanguage language, TranslationProvider provider) {
            this.translationProviders.put(language, provider);
            return this;
        }

        public Builder withDefaultLanguage(SupportedLanguage language) {
            this.defaultLanguage = language;
            return this;
        }

        public Builder withDynamicLanguageLoading(boolean dynamicLanguageLoading) {
            this.dynamicLanguageLoading = dynamicLanguageLoading;
            return this;
        }

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
