package de.MCmoderSD.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

@SuppressWarnings("unused")
public class JsonReader {

    // Attributes
    private final ObjectMapper mapper;
    private final HashMap<String, JsonNode> jsonCache;
    private boolean isAbsolute;

    // Constructor without isAbsolute
    public JsonReader() {
        isAbsolute = false;
        mapper = new ObjectMapper();
        jsonCache = new HashMap<>();
    }

    // Constructor with isAbsolute
    public JsonReader(boolean isAbsolute) {
        this.isAbsolute = isAbsolute;
        mapper = new ObjectMapper();
        jsonCache = new HashMap<>();
    }

    // Read JSON file and return JsonNode
    public JsonNode read(String json) {
        if (jsonCache.containsKey(json)) return jsonCache.get(json); // Checks if the path has already been loaded

        InputStream inputStream;

        try {
            if (isAbsolute) inputStream = Files.newInputStream(Paths.get(json)); // JSON is Local
            else inputStream = getClass().getResourceAsStream(json); // JSON is in Jar

            // Null check
            if (inputStream == null) throw new IllegalArgumentException("The JSON file could not be found: " + json);

            // Write to cache
            JsonNode node = mapper.readTree(inputStream);
            jsonCache.put(json, node);

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

    public void clearCache(String json) {
        jsonCache.remove(json);
    }

    public void switchMode() {
        isAbsolute = !isAbsolute;
    }

    public void setAbsolute(boolean isAbsolute) {
        this.isAbsolute = isAbsolute;
    }

    // Getter
    public HashMap<String, JsonNode> getJsonCache() {
        return jsonCache;
    }

    public boolean isAbsolute() {
        return isAbsolute;
    }
}