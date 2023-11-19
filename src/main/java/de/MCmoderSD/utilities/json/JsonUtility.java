package de.MCmoderSD.utilities.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

@SuppressWarnings("unused")
public abstract class JsonUtility {

    // Attributes
    protected ObjectMapper mapper;
    protected HashMap<String, JsonNode> jsonCache;
    protected String url;
    protected boolean isAbsolute;

    // Constructor without isAbsolute
    public JsonUtility() {
        isAbsolute = false;
        mapper = new ObjectMapper();
        jsonCache = new HashMap<>();
    }

    // Constructor with URL
    public JsonUtility(String url) {
        this.isAbsolute = false;
        this.url = url;
        mapper = new ObjectMapper();
        jsonCache = new HashMap<>();
    }

    // Constructor with isAbsolute
    public JsonUtility(boolean isAbsolute) {
        this.isAbsolute = isAbsolute;
        this.url = null;
        mapper = new ObjectMapper();
        jsonCache = new HashMap<>();
    }

    // Read JSON file and return JsonNode
    public abstract JsonNode read(String json);

    // Setter
    public void clearCache() {
        jsonCache.clear();
    }

    public void clearCache(String json) {
        jsonCache.remove(json);
    }

    public void addJson(String resource, JsonNode json) {
        jsonCache.put(resource, json);
    }

    public void switchMode() {
        isAbsolute = !isAbsolute;
    }

    public void setAbsolute(boolean isAbsolute) {
        this.isAbsolute = isAbsolute;
    }

    public void setURL(String url) {
        this.url = url;
    }

    // Getter
    public HashMap<String, JsonNode> getJsonCache() {
        return jsonCache;
    }

    public String getURL() {
        return url;
    }

    public JsonNode getJson(String json) {
        return jsonCache.get(json);
    }

    public boolean isAbsolute() {
        return isAbsolute;
    }
}
