package ru.dverkask.polyglotmc.version;

import java.util.Objects;

/**
 * Represents a Minecraft version in the PolyglotMC library.
 * <p>
 * This interface is implemented by various enums in the {@link SupportedVersion} class
 * to represent different types of Minecraft versions, such as releases, snapshots, and pre-releases.
 * </p>
 * <p>
 * Implementations of this interface should provide a string representation of the Minecraft version.
 * </p>
 *
 * @author DverkaSK
 * @version 1.0
 * @since 1.0
 * @see SupportedVersion
 * @see SupportedVersion.Release
 * @see SupportedVersion.Snapshot
 * @see SupportedVersion.PreRelease
 */
public interface Version {

    /**
     * Returns the string representation of the Minecraft version.
     *
     * @return a {@link String} representing the Minecraft version
     */
    String version();

    /**
     * Tells whether PolyglotMC can load language files of this version.
     * <p>
     * Only Minecraft 1.13 (snapshot 17w43a) and newer ship JSON language files; older versions use
     * the legacy {@code .lang} format and are not supported. Version strings of an unknown format
     * are assumed to be supported.
     * </p>
     *
     * @return {@code true} if language files of this version can be loaded
     * @since 2.1
     */
    default boolean isSupported() {
        return SupportedVersion.hasJsonLanguageFiles(version());
    }

    /**
     * Creates a version from an arbitrary string.
     * <p>
     * Useful for versions that are not yet listed in {@link SupportedVersion}.
     * </p>
     *
     * @param version the string representation of the Minecraft version, e.g. {@code "1.21.11"}
     * @return a {@link Version} with the given string representation
     */
    static Version of(String version) {
        Objects.requireNonNull(version, "version");
        return SupportedVersion.findByVersion(version).orElse(() -> version);
    }
}
