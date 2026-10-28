package ru.dverkask.polyglotmc.api;

import org.junit.jupiter.api.Test;
import ru.dverkask.polyglotmc.lang.SupportedLanguage;
import ru.dverkask.polyglotmc.version.SupportedVersion;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Builder and fallback logic with in-memory providers, no network. */
class PolyglotTest {

    private static final String SWORD = "item.minecraft.diamond_sword";
    private static final String CREEPER = "entity.minecraft.creeper";

    private static TranslationProvider provider(Map<String, String> translations) {
        return key -> Optional.ofNullable(translations.get(key));
    }

    private static Polyglot polyglot() {
        return new Polyglot.Builder()
                .withVersion(SupportedVersion.Release.V1_20_4)
                .withTranslationProvider(SupportedLanguage.EN_US, provider(Map.of(SWORD, "Diamond Sword", CREEPER, "Creeper")))
                .withTranslationProvider(SupportedLanguage.RU_RU, provider(Map.of(SWORD, "Алмазный меч")))
                .build();
    }

    @Test
    void translatesIntoRequestedLanguage() {
        TranslationResult result = polyglot().translate(SWORD, SupportedLanguage.RU_RU);

        assertEquals(new TranslationResult(SWORD, "Алмазный меч", SupportedLanguage.RU_RU, true), result);
    }

    @Test
    void translatesTranslatableObjects() {
        assertEquals("Алмазный меч", polyglot().translate(() -> SWORD, SupportedLanguage.RU_RU).translatedName());
        assertEquals("Diamond Sword", polyglot().translate(() -> SWORD).translatedName());
    }

    @Test
    void fallsBackToDefaultLanguage() {
        TranslationResult result = polyglot().translate(CREEPER, SupportedLanguage.RU_RU);

        assertEquals("Creeper", result.translatedName());
        assertFalse(result.isTranslated());
    }

    @Test
    void fallsBackToDefaultLanguageForLanguagesWithoutProvider() {
        TranslationResult result = polyglot().translate(SWORD, SupportedLanguage.DE_DE);

        assertEquals("Diamond Sword", result.translatedName());
        assertFalse(result.isTranslated());
    }

    @Test
    void fallsBackToKey() {
        assertEquals("missing.key", polyglot().translate("missing.key", SupportedLanguage.RU_RU).translatedName());
        assertEquals("missing.key", polyglot().translate("missing.key").translatedName());
    }

    @Test
    void translatesIntoDefaultLanguage() {
        Polyglot polyglot = polyglot();

        assertEquals(SupportedLanguage.EN_US, polyglot.getDefaultLanguage());
        assertTrue(polyglot.translate(CREEPER).isTranslated());
        assertEquals(SupportedVersion.Release.V1_20_4, polyglot.getVersion());
    }

    @Test
    void requiresLanguages() {
        assertThrows(IllegalStateException.class, () -> new Polyglot.Builder().build());
    }

    @Test
    void requiresDefaultLanguageAmongLanguages() {
        Polyglot.Builder builder = new Polyglot.Builder()
                .withTranslationProvider(SupportedLanguage.RU_RU, provider(Map.of()));

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void rejectsNulls() {
        Polyglot.Builder builder = new Polyglot.Builder();

        assertThrows(NullPointerException.class, () -> builder.withLanguages(SupportedLanguage.EN_US, null));
        assertThrows(NullPointerException.class, () -> builder.withTranslationProvider(null, provider(Map.of())));
        assertThrows(NullPointerException.class, () -> builder.withTranslationProvider(SupportedLanguage.EN_US, null));
        assertThrows(NullPointerException.class, () -> polyglot().translate((String) null));
    }

    @Test
    @SuppressWarnings("removal")
    void rejectsVersionsWithoutJsonLanguageFiles() {
        assertThrows(IllegalArgumentException.class,
                () -> new Polyglot.Builder().withVersion(SupportedVersion.Release.V1_12_2));
    }
}
