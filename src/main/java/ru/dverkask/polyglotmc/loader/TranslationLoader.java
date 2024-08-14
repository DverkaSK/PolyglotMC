package ru.dverkask.polyglotmc.loader;

import org.bukkit.Material;
import ru.dverkask.polyglotmc.lang.SupportedLanguage;
import ru.dverkask.polyglotmc.version.Version;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.AbstractMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Responsible for loading translations for Minecraft items and blocks from online JSON files.
 * <p>
 * This class fetches and parses translation data for a specific language and Minecraft version.
 * It uses the InventivetalentDev's minecraft-assets repository as the source of translation files.
 * </p>
 *
 * @author DverkaSK
 * @version 1.0
 * @since 1.0
 */
public class TranslationLoader {

    /**
     * The base URL for fetching Minecraft language assets.
     * This URL points to the InventivetalentDev's minecraft-assets repository.
     */
    private static final String BASE_URL = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/%s/assets/minecraft/lang/%s.json";

    /**
     * A regular expression pattern used to parse the JSON content of language files.
     * This pattern matches keys for blocks and items, along with their translations.
     */
    private static final Pattern JSON_PATTERN = Pattern.compile("\"((?:block|item)\\.minecraft\\.[^\"]+)\"\\s*:\\s*\"([^\"]+)\"");

    /**
     * The language for which translations are being loaded.
     * This field determines which language file will be fetched and parsed.
     */
    private final SupportedLanguage language;

    /**
     * The Minecraft version for which translations are being loaded.
     * This field may be used to adjust the loading process for different game versions.
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
     * Loads translations for the specified language and version.
     * <p>
     * This method fetches the JSON content from the online source and parses it
     * to create a mapping between {@link Material} and its translated name.
     * </p>
     *
     * @return A {@link Map} where keys are {@link Material} objects and values are their translated names.
     *         Returns an empty map if an error occurs during loading.
     */
    public Map<Material, String> loadTranslations() {
        try {
            String jsonContent = fetchJsonContent();
            return parseJson(jsonContent);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Map.of();
        }
    }

    /**
     * Fetches the JSON content from the online source.
     *
     * @return A {@link String} containing the JSON content.
     * @throws IOException If an I/O error occurs when sending or receiving the HTTP request.
     * @throws InterruptedException If the operation is interrupted.
     */
    private String fetchJsonContent() throws IOException, InterruptedException {
        String url = String.format(BASE_URL, version.version(), language.getCode());
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * Parses the JSON content and creates a mapping between Materials and their translated names.
     *
     * @param jsonContent The JSON content to parse.
     * @return A {@link Map} where keys are {@link Material} objects and values are their translated names.
     */
    private Map<Material, String> parseJson(String jsonContent) {
        return Pattern.compile(String.valueOf(JSON_PATTERN))
                .matcher(jsonContent)
                .results()
                .map(matchResult -> new String[]{matchResult.group(1), matchResult.group(2)})
                .filter(groups -> groups[0].split("\\.").length >= 3)
                .flatMap(groups -> {
                    try {
                        Material material = Material.valueOf(groups[0].split("\\.")[2].toUpperCase());
                        return Stream.of(new AbstractMap.SimpleEntry<>(material, groups[1]));
                    } catch (IllegalArgumentException e) {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> existing
                ));
    }
}
