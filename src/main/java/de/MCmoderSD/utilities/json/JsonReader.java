package de.MCmoderSD.utilities.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

@SuppressWarnings("unused")
public class JsonReader extends JsonUtility {

    // Constructor without isAbsolute
    public JsonReader() {
        isAbsolute = false;
        mapper = new ObjectMapper();
        jsonCache = new HashMap<>();
    }

    // Constructor with isAbsolute
    public JsonReader(boolean isAbsolute) {
        this.isAbsolute = isAbsolute;
        this.url = null;
        mapper = new ObjectMapper();
        jsonCache = new HashMap<>();
    }

    // Read JSON file and return JsonNode
    @Override
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

    public JsonNode read(String json, boolean isAbsolute) {
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
}