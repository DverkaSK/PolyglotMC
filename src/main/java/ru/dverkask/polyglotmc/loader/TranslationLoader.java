package ru.dverkask.polyglotmc.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import ru.dverkask.polyglotmc.lang.SupportedLanguage;
import ru.dverkask.polyglotmc.version.Version;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for loading Minecraft language files from online JSON sources.
 * <p>
 * This class fetches and parses the complete language file for a specific language and Minecraft version,
 * so every translation key of the game (items, blocks, entities, enchantments, effects, biomes, etc.) is available.
 * It uses the InventivetalentDev's minecraft-assets repository as the source of translation files.
 * Only versions that ship JSON language files (1.13 and newer) are supported.
 * </p>
 *
 * @author DverkaSK
 * @version 2.0
 * @since 1.0
 */
public class TranslationLoader {

    /**
     * The base URL for fetching Minecraft language assets.
     * This URL points to the InventivetalentDev's minecraft-assets repository.
     */
    private static final String BASE_URL = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/%s/assets/minecraft/lang/%s.json";

    /**
     * Shared HTTP client used for all requests.
     */
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    /**
     * The language for which translations are being loaded.
     */
    private final SupportedLanguage language;

    /**
     * The Minecraft version for which translations are being loaded.
     */
    private final Version version;

    /**
     * Constructs a new TranslationLoader for the specified language and version.
     *
     * @param language The {@link SupportedLanguage} for which to load translations.
     * @param version The {@link Version} of Minecraft for which to load translations.
     */
    public TranslationLoader(SupportedLanguage language, Version version) {
        this.language = language;
        this.version = version;
    }

    /**
     * Loads all translations for the specified language and version.
     *
     * @return A {@link Map} where keys are translation keys (e.g. {@code item.minecraft.diamond_sword})
     *         and values are their translated strings.
     * @throws IOException If the file could not be downloaded or parsed.
     * @throws InterruptedException If the operation is interrupted.
     */
    public Map<String, String> loadTranslations() throws IOException, InterruptedException {
        return parseJson(fetchJsonContent());
    }

    /**
     * Fetches the JSON content from the online source.
     *
     * @return A {@link String} containing the JSON content.
     * @throws IOException If an I/O error occurs or the server responds with a non-200 status code.
     * @throws InterruptedException If the operation is interrupted.
     */
    private String fetchJsonContent() throws IOException, InterruptedException {
        String url = String.format(BASE_URL, version.version(), language.getCode());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Failed to fetch " + url + ": HTTP " + response.statusCode());
        }
        return response.body();
    }

    /**
     * Parses the JSON content into a map of translation keys and their translated strings.
     *
     * @param jsonContent The JSON content to parse.
     * @return A {@link Map} where keys are translation keys and values are their translated strings.
     * @throws IOException If the content is not a valid language file.
     */
    private Map<String, String> parseJson(String jsonContent) throws IOException {
        try {
            JsonObject root = JsonParser.parseString(jsonContent).getAsJsonObject();
            Map<String, String> translations = new HashMap<>(root.size() * 2);
            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                if (entry.getValue().isJsonPrimitive()) {
                    translations.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
            return translations;
        } catch (JsonParseException | IllegalStateException e) {
            throw new IOException("Invalid language file for " + language.getCode() + " / " + version.version(), e);
        }
    }
}
