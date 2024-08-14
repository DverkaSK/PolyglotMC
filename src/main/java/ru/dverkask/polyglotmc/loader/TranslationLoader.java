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

public class TranslationLoader {

    private static final String BASE_URL = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/%s/assets/minecraft/lang/%s.json";
    private static final Pattern JSON_PATTERN = Pattern.compile("\"((?:block|item)\\.minecraft\\.[^\"]+)\"\\s*:\\s*\"([^\"]+)\"");
    private final SupportedLanguage language;
    private final Version version;

    public TranslationLoader(SupportedLanguage language, Version version) {
        this.language = language;
        this.version = version;
    }

    public Map<Material, String> loadTranslations() {
        try {
            String jsonContent = fetchJsonContent();
            return parseJson(jsonContent);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Map.of();
        }
    }

    private String fetchJsonContent() throws IOException, InterruptedException {
        String url = String.format(BASE_URL, version.version(), language.getCode());
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

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
