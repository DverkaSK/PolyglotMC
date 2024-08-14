package ru.dverkask.polyglotmc.api;

import ru.dverkask.polyglotmc.lang.SupportedLanguage;

/**
 * Represents the result of a translation operation in the PolyglotMC library.
 * <p>
 * This record encapsulates all relevant information about a translation attempt,
 * including the original item name, the translated name, the target language,
 * and whether the translation was successful.
 * </p>
 * <p>
 * It is typically returned by the {@link ru.dverkask.polyglotmc.api.Polyglot#translate(org.bukkit.Material, SupportedLanguage)}
 * method and can be used to access the translation results in a structured manner.
 * </p>
 *
 * @param originalName The original name of the Minecraft item before translation.
 * @param translatedName The translated name of the Minecraft item. If translation failed, this may be the same as the original name.
 * @param language The {@link SupportedLanguage} into which the item name was translated.
 * @param isTranslated A boolean indicating whether the translation was successful.
 *
 * @author DverkaSK
 * @version 1.0
 * @since 1.0
 * @see ru.dverkask.polyglotmc.api.Polyglot
 * @see ru.dverkask.polyglotmc.lang.SupportedLanguage
 */
public record TranslationResult(
        String originalName,
        String translatedName,
        SupportedLanguage language,
        boolean isTranslated
) {}
