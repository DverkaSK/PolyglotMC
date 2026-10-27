package ru.dverkask.polyglotmc.api;

import java.util.Optional;

/**
 * The TranslationProvider interface defines the contract for providing translations
 * of Minecraft translation keys in the PolyglotMC library.
 * <p>
 * Implementations of this interface are responsible for resolving a translation key
 * (e.g. {@code item.minecraft.diamond_sword} or {@code enchantment.minecraft.sharpness})
 * into a human-readable string. The primary use of this interface is within the
 * {@link ru.dverkask.polyglotmc.api.Polyglot} class.
 * </p>
 * <p>
 * The default implementation provided by the library is
 * {@link ru.dverkask.polyglotmc.provider.CacheTranslationProvider CacheTranslationProvider}, which uses
 * a cached map of translations for efficient lookup.
 * </p>
 *
 * @author DverkaSK
 * @version 2.0
 * @since 1.0
 * @see ru.dverkask.polyglotmc.api.Polyglot
 * @see ru.dverkask.polyglotmc.provider.CacheTranslationProvider CacheTranslationProvider
 */
public interface TranslationProvider {

    /**
     * Retrieves the translation for a given translation key.
     *
     * @param key The translation key, e.g. {@code item.minecraft.diamond_sword}.
     * @return An {@link Optional} containing the translated string if available,
     *         or an empty Optional if no translation is found.
     * @throws NullPointerException if the provided key is null.
     */
    Optional<String> getTranslation(String key);
}
