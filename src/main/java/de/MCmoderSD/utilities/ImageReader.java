package de.MCmoderSD.utilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings("unused")
public class ImageReader {

    // Attributes
    private final HashMap<String, BufferedImage> bufferedImageCache;
    private boolean isAbsolute;

    // Constructor without isAbsolute
    public ImageReader() {
        isAbsolute = false;
        bufferedImageCache = new HashMap<>();
    }

    // Constructor with isAbsolute
    public ImageReader(boolean isAbsolute) {
        this.isAbsolute = isAbsolute;
        bufferedImageCache = new HashMap<>();
    }

    // Read image file and return BufferedImage
    public BufferedImage read(String resource) {
        if (bufferedImageCache.containsKey(resource)) return bufferedImageCache.get(resource); // Checks the cache for the image

        BufferedImage image = null;
        try {
            if (resource.endsWith(".png")) {
                if (isAbsolute) image = ImageIO.read(Files.newInputStream(Paths.get(resource))); // Image is local
                else image = ImageIO.read((Objects.requireNonNull(getClass().getResource(resource)))); // Image is in the JAR file
            } else throw new IllegalArgumentException("The image format is not supported: " + resource); // Image format is not supported
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        // Add to cache
        bufferedImageCache.put(resource, image);

        // Null check
        if (image == null) throw new IllegalArgumentException("The image could not be loaded: " + resource);
        return image;
    }


    // Setter
    public void clearCache() {
        bufferedImageCache.clear();
    }

    public void clearCache(String resource) {
        bufferedImageCache.remove(resource);
    }

    public void switchMode() {
        isAbsolute = !isAbsolute;
    }

    public void setAbsolute(boolean isAbsolute) {
        this.isAbsolute = isAbsolute;
    }

    // Getter
    public HashMap<String, BufferedImage> getBufferedImageCache() {
        return bufferedImageCache;
    }

    public boolean isAbsolute() {
        return isAbsolute;
    }
}
