package de.MCmoderSD.utilities.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

@SuppressWarnings("unused")
public class JsonStreamer extends JsonUtility {

    // Constructor without isAbsolute
    public JsonStreamer() {
        isAbsolute = false;
        mapper = new ObjectMapper();
        jsonCache = new HashMap<>();
    }

    // Constructor with URL
    public JsonStreamer(String url) {
        this.isAbsolute = false;
        this.url = url;
        mapper = new ObjectMapper();
        jsonCache = new HashMap<>();
    }

    // Read JSON file and return JsonNode
    @Override
    public JsonNode read(String resource) {
        if (jsonCache.containsKey(resource))
            return jsonCache.get(resource); // Checks if the path has already been loaded

        URL json;

        try {
            if (this.url != null) json = new URL(url + resource);
            else json = new URL(resource);

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

    public JsonNode read(String url, String resource) {
        if (jsonCache.containsKey(resource))
            return jsonCache.get(resource); // Checks if the path has already been loaded

        try {
            URL json = new URL(url + resource);
            InputStream inputStream = json.openStream();

            // Null check
            if (inputStream == null) throw new IllegalArgumentException("The JSON file could not be found: " + json);

            // Write to cache
            JsonNode node = mapper.readTree(inputStream);
            jsonCache.put(resource, node);

            // return
            return node;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}