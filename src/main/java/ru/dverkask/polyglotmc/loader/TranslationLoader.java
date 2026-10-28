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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Responsible for loading Minecraft language files from online JSON sources.
 * <p>
 * This class fetches and parses the complete language file for a specific language and Minecraft version,
 * so every translation key of the game (items, blocks, entities, enchantments, effects, biomes, etc.) is available.
 * It uses the InventivetalentDev's minecraft-assets repository as the source of translation files.
 * Only versions that ship JSON language files (1.13 and newer) are supported.
 * </p>
 * <p>
 * If a cache directory is given, downloaded files are stored there as {@code <version>/<language>.json}
 * and read from disk on later loads. Language files of a game version never change, so the cache needs no expiry.
 * </p>
 *
 * @author DverkaSK
 * @version 2.1
 * @since 1.0
 */
public class TranslationLoader {

    private static final Logger LOGGER = Logger.getLogger("PolyglotMC");

    /**
     * The base URL for fetching Minecraft language assets.
     * This URL points to the InventivetalentDev's minecraft-assets repository.
     */
    private static final String BASE_URL = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/%s/assets/minecraft/lang/%s.json";

    /**
     * Characters allowed in a version string. It becomes part of a URL and of a file path.
     */
    private static final Pattern SAFE_VERSION = Pattern.compile("[A-Za-z0-9][A-Za-z0-9._-]*");

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
     * The directory language files are cached in, or {@code null} to always download them.
     */
    private final Path cacheDirectory;

    /**
     * Constructs a new TranslationLoader for the specified language and version.
     *
     * @param language The {@link SupportedLanguage} for which to load translations.
     * @param version The {@link Version} of Minecraft for which to load translations.
     */
    public TranslationLoader(SupportedLanguage language, Version version) {
        this(language, version, null);
    }

    /**
     * Constructs a new TranslationLoader that caches language files on disk.
     *
     * @param language The {@link SupportedLanguage} for which to load translations.
     * @param version The {@link Version} of Minecraft for which to load translations.
     * @param cacheDirectory The directory to cache language files in, or {@code null} to always download them.
     * @since 2.1
     */
    public TranslationLoader(SupportedLanguage language, Version version, Path cacheDirectory) {
        this.language = language;
        this.version = version;
        this.cacheDirectory = cacheDirectory;
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
        if (!SAFE_VERSION.matcher(version.version()).matches()) {
            throw new IOException("Invalid version string: " + version.version());
        }
        if (!version.isSupported()) {
            throw new IOException("Minecraft " + version.version()
                    + " uses the legacy .lang format, only 1.13 and newer are supported");
        }

        Path cacheFile = cacheDirectory == null
                ? null
                : cacheDirectory.resolve(version.version()).resolve(language.getCode() + ".json");
        if (cacheFile != null && Files.isRegularFile(cacheFile)) {
            try {
                return parseJson(Files.readString(cacheFile, StandardCharsets.UTF_8));
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Ignoring unreadable cached language file " + cacheFile, e);
            }
        }

        String json = fetchJsonContent();
        Map<String, String> translations = parseJson(json);
        if (cacheFile != null) {
            writeCache(cacheFile, json);
        }
        return translations;
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
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() != 200) {
            throw new IOException("Failed to fetch " + url + ": HTTP " + response.statusCode());
        }
        return response.body();
    }

    /**
     * Stores a downloaded language file. A failure is only logged: the translations are already loaded.
     * The file is written under a temporary name and moved into place, so a crash never leaves a partial file.
     */
    private void writeCache(Path cacheFile, String json) {
        try {
            Files.createDirectories(cacheFile.getParent());
            Path temp = Files.createTempFile(cacheFile.getParent(), language.getCode(), ".tmp");
            try {
                Files.writeString(temp, json, StandardCharsets.UTF_8);
                Files.move(temp, cacheFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } finally {
                Files.deleteIfExists(temp);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to cache language file " + cacheFile, e);
        }
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
            if (translations.isEmpty()) {
                throw new IOException("Empty language file for " + language.getCode() + " / " + version.version());
            }
            return translations;
        } catch (JsonParseException | IllegalStateException e) {
            throw new IOException("Invalid language file for " + language.getCode() + " / " + version.version(), e);
        }
    }
}
