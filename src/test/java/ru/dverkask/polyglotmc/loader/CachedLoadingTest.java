package ru.dverkask.polyglotmc.loader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.dverkask.polyglotmc.api.Polyglot;
import ru.dverkask.polyglotmc.api.TranslationResult;
import ru.dverkask.polyglotmc.lang.SupportedLanguage;
import ru.dverkask.polyglotmc.version.Version;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Loading through the disk cache. Each test uses its own made-up version, so the shared in-memory cache
 * of {@link TranslationManager} never leaks between tests, and language files come from the cache directory.
 */
class CachedLoadingTest {

    private static final String SWORD = "item.minecraft.diamond_sword";

    @TempDir
    Path cacheDirectory;

    private static Version uniqueVersion() {
        return Version.of("test-" + UUID.randomUUID());
    }

    private void cache(Version version, SupportedLanguage language, String json) throws IOException {
        Path file = cacheDirectory.resolve(version.version()).resolve(language.getCode() + ".json");
        Files.createDirectories(file.getParent());
        Files.writeString(file, json, StandardCharsets.UTF_8);
    }

    @Test
    void readsLanguageFilesFromCacheDirectory() throws IOException {
        Version version = uniqueVersion();
        cache(version, SupportedLanguage.EN_US, "{\"" + SWORD + "\": \"Diamond Sword\"}");
        cache(version, SupportedLanguage.RU_RU, "{\"" + SWORD + "\": \"Алмазный меч\", \"nested\": {}}");

        Polyglot polyglot = new Polyglot.Builder()
                .withVersion(version)
                .withLanguages(SupportedLanguage.EN_US, SupportedLanguage.RU_RU)
                .withCacheDirectory(cacheDirectory)
                .build();

        assertEquals("Алмазный меч", polyglot.translate(SWORD, SupportedLanguage.RU_RU).translatedName());
        assertEquals("Diamond Sword", polyglot.translate(SWORD).translatedName());
    }

    @Test
    void loadsDynamicLanguagesInBackground() throws Exception {
        Version version = uniqueVersion();
        cache(version, SupportedLanguage.EN_US, "{\"" + SWORD + "\": \"Diamond Sword\"}");
        cache(version, SupportedLanguage.DE_DE, "{\"" + SWORD + "\": \"Diamantschwert\"}");

        Polyglot polyglot = new Polyglot.Builder()
                .withVersion(version)
                .withLanguages(SupportedLanguage.EN_US)
                .withCacheDirectory(cacheDirectory)
                .withDynamicLanguageLoading(true)
                .build();

        TranslationResult result = polyglot.translate(SWORD, SupportedLanguage.DE_DE);
        long deadline = System.currentTimeMillis() + 5_000;
        while (!result.isTranslated() && System.currentTimeMillis() < deadline) {
            assertEquals("Diamond Sword", result.translatedName());
            Thread.sleep(20);
            result = polyglot.translate(SWORD, SupportedLanguage.DE_DE);
        }
        assertEquals("Diamantschwert", result.translatedName());
        assertTrue(result.isTranslated());
    }

    @Test
    void failsBuildWhenLanguageFileCannotBeLoaded() throws IOException {
        Version version = uniqueVersion();
        cache(version, SupportedLanguage.EN_US, "not json");
        Polyglot.Builder builder = new Polyglot.Builder()
                .withVersion(version)
                .withLanguages(SupportedLanguage.EN_US)
                .withCacheDirectory(cacheDirectory);

        // The broken cached file is ignored and the made-up version is missing online (or the network is down).
        assertThrows(UncheckedIOException.class, builder::build);

        // The failure is remembered: the next attempt fails at once instead of going to the network.
        UncheckedIOException again = assertThrows(UncheckedIOException.class, builder::build);
        assertTrue(again.getCause().getMessage().contains("seconds ago"), again.getCause().getMessage());
        assertFalse(TranslationManager.getInstance().request(SupportedLanguage.EN_US, version, cacheDirectory).isPresent());
    }

    @Test
    void rejectsVersionStringsThatEscapeTheCacheDirectory() {
        IOException e = assertThrows(IOException.class,
                () -> TranslationManager.getInstance().load(SupportedLanguage.EN_US, Version.of("../evil"), cacheDirectory));
        assertTrue(e.getMessage().contains("Invalid version"), e.getMessage());
    }
}
