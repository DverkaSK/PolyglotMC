package ru.dverkask.polyglotmc.api;

import ru.dverkask.polyglotmc.lang.SupportedLanguage;

public record TranslationResult(
        String originalName,
        String translatedName,
        SupportedLanguage language,
        boolean isTranslated
) {}
