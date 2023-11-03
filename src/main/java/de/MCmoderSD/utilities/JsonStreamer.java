package de.MCmoderSD.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

@SuppressWarnings("unused")
public class JsonStreamer {

    // Attributes
    private final ObjectMapper mapper;
    private final HashMap<String, JsonNode> jsonCache;

    // Constructor
    public JsonStreamer() {
        mapper = new ObjectMapper();
        jsonCache = new HashMap<>();
    }

    // Read JSON file and return JsonNode
    public JsonNode read(String url) {
        if (jsonCache.containsKey(url)) return jsonCache.get(url); // Checks if the path has already been loaded

        try {
            URL json = new URL(url);
            InputStream inputStream = json.openStream();

            // Null check
            if (inputStream == null) throw new IllegalArgumentException("The JSON file could not be found: " + json);

            // Write to cache
            JsonNode node = mapper.readTree(inputStream);
            jsonCache.put(url, node);

            // return
            return node;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Setter
    public void clearCache() {
        jsonCache.clear();
    }

    public void clearCache(String url) {
        jsonCache.remove(url);
    }

    // Getter
    public HashMap<String, JsonNode> getJsonCache() {
        return jsonCache;
    }
}