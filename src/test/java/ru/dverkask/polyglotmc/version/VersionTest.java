package ru.dverkask.polyglotmc.version;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class VersionTest {

    @ParameterizedTest
    @CsvSource({
            "1.13, true", "1.13.2, true", "1.21.11, true", "26.1, true", "26.1.2, true",
            "1.12.2, false", "1.8.9, false", "1.0, false",
            "1.20.5-pre3, true", "1.21.1-rc1, true", "1.12-pre5, false",
            "17w43a, true", "18w02a, true", "24w21a, true", "21W10A, true",
            "17w18b, false", "13w37a, false",
            "some-custom-build, true"
    })
    void detectsVersionsWithJsonLanguageFiles(String version, boolean supported) {
        assertEquals(supported, Version.of(version).isSupported(), version);
    }

    @Test
    void exactlyTheUnsupportedConstantsAreDeprecated() throws NoSuchFieldException {
        for (Enum<?> constant : allConstants()) {
            boolean deprecated = constant.getDeclaringClass().getField(constant.name()).isAnnotationPresent(Deprecated.class);
            assertEquals(!((Version) constant).isSupported(), deprecated, constant.name());
        }
    }

    @Test
    void versionStringsAreLowerCase() {
        // Version strings are branch names in a case-sensitive URL.
        for (Enum<?> constant : allConstants()) {
            String version = ((Version) constant).version();
            assertEquals(version.toLowerCase(), version, constant.name());
        }
    }

    @Test
    void ofReturnsListedConstants() {
        assertSame(SupportedVersion.Release.V1_20_4, Version.of("1.20.4"));
        assertSame(SupportedVersion.Snapshot.SNAPSHOT_21W10A, Version.of("21W10A"));
        assertEquals("27.0", Version.of("27.0").version());
    }

    private static Enum<?>[] allConstants() {
        return Stream.of(
                SupportedVersion.Release.values(),
                SupportedVersion.Snapshot.values(),
                SupportedVersion.PreRelease.values()
        ).flatMap(Stream::of).toArray(Enum<?>[]::new);
    }
}
