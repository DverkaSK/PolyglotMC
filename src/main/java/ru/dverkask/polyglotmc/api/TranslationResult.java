package ru.dverkask.polyglotmc.api;

import ru.dverkask.polyglotmc.lang.SupportedLanguage;

/**
 * Represents the result of a translation operation in the PolyglotMC library.
 * <p>
 * This record encapsulates all relevant information about a translation attempt,
 * including the translation key, the translated string, the target language,
 * and whether the translation was found in the target language.
 * </p>
 *
 * @param key The translation key that was translated, e.g. {@code item.minecraft.diamond_sword}.
 * @param translatedName The translated string. If the key is missing in the target language, this is the
 *                       translation in the default language, or the key itself if that is missing too.
 * @param language The {@link SupportedLanguage} that was requested.
 * @param isTranslated Whether the translation was found in the requested language.
 *
 * @author DverkaSK
 * @version 2.0
 * @since 1.0
 * @see ru.dverkask.polyglotmc.api.Polyglot
 * @see ru.dverkask.polyglotmc.lang.SupportedLanguage
 */
public record TranslationResult(
        String key,
        String translatedName,
        SupportedLanguage language,
        boolean isTranslated
) {}
