package ru.dverkask.polyglotmc.api;

import org.bukkit.Material;

import java.util.Optional;

/**
 * The TranslationProvider interface defines the contract for providing translations
 * of Minecraft item names in the PolyglotMC library.
 * <p>
 * Implementations of this interface are responsible for retrieving translations
 * for given Minecraft materials. The primary use of this interface is within the
 * {@link ru.dverkask.polyglotmc.api.Polyglot} class to provide multilingual support
 * for Minecraft items.
 * </p>
 * <p>
 * The default implementation provided by the library is
 * {@link ru.dverkask.polyglotmc.provider.CacheTranslationProvider CacheTranslationProvider}, which uses
 * a cached map of translations for efficient lookup.
 * </p>
 *
 * @author DverkaSK
 * @version 1.0
 * @since 1.0
 * @see ru.dverkask.polyglotmc.api.Polyglot
 * @see ru.dverkask.polyglotmc.provider.CacheTranslationProvider CacheTranslationProvider
 */
public interface TranslationProvider {

    /**
     * Retrieves the translation for a given Minecraft material.
     * <p>
     * This method should return an {@link Optional} containing the translated
     * name of the item if a translation is available, or an empty Optional if
     * no translation is found.
     * </p>
     *
     * @param item The {@link Material} for which to retrieve the translation.
     * @return An {@link Optional} containing the translated name if available,
     *         or an empty Optional if no translation is found.
     * @throws NullPointerException if the provided item is null.
     */
    Optional<String> getTranslation(Material item);
}
