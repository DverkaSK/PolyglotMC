package ru.dverkask.polyglotmc.version;

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
}
