package de.MCmoderSD;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public class Utils { // Class is used for convenient calculations
    // Cache
    private final HashMap<String, BufferedImage> bufferedImageCache = new HashMap<>(); // Cache for BufferedImages

    // Methods

    // Read JSON file
    public JsonNode readJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream inputStream = getClass().getResourceAsStream(json); // JSON is in Jar
            if (inputStream == null) throw new IllegalArgumentException("The JSON file could not be found: " + json);
            return mapper.readTree(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Center JFrame
    public Point centerFrame(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Screen Size
        int x = ((screenSize.width - frame.getWidth()) / 2);
        int y = ((screenSize.height - frame.getHeight()) / 2);
        return new Point(x, y);
    }

    // Load Image
    public BufferedImage reader(String resource) {
        if (bufferedImageCache.containsKey(resource)) return bufferedImageCache.get(resource); // Checks if the path has already been loaded
        BufferedImage image = null;
        try {
            if (resource.endsWith(".png")) { // Checks if the image is a .png
                image = ImageIO.read((Objects.requireNonNull(getClass().getResource(resource)))); // Image is in the JAR file
            } else throw new IllegalArgumentException("The image format is not supported: " + resource);

            bufferedImageCache.put(resource, image); // Adds the image to the cache

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        if (image == null) throw new IllegalArgumentException("The image could not be loaded: " + resource);
        return image;
    }
}