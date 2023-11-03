package de.MCmoderSD.utilities;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

@SuppressWarnings("unused")
public class ImageStreamer {

    // Attributes
    private final HashMap<String, BufferedImage> bufferedImageCache;

    // Constructor
    public ImageStreamer() {
        bufferedImageCache = new HashMap<>();
    }


    // Read image file and return BufferedImage
    public BufferedImage read(String url) {
        if (bufferedImageCache.containsKey(url)) return bufferedImageCache.get(url); // Checks the cache for the image

        BufferedImage image = null;
        try {
            URL resource = new URL(url);
            if (url.endsWith(".png")) image = ImageIO.read(resource);
            else throw new IllegalArgumentException("The image format is not supported: " + url); // Image format is not supported
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        // Add to cache
        bufferedImageCache.put(url, image);

        // Null check
        if (image == null) throw new IllegalArgumentException("The image could not be loaded: " + url);
        return image;
    }


    // Image scaling

    public BufferedImage scaleImage(BufferedImage image, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        scaledImage.getGraphics().drawImage(image, 0, 0, width, height, null);
        return scaledImage;
    }

    public BufferedImage scaleImage(BufferedImage image, int scale) {
        return scaleImage(image, scale, scale);
    }

    public BufferedImage scaleImage(String url, int width, int height) {
        return scaleImage(read(url), width, height);
    }

    public BufferedImage scaleImage(String url, int scale) {
        return scaleImage(url, scale, scale);
    }

    // Create ImageIcon
    public ImageIcon createImageIcon(BufferedImage image) {
        return new ImageIcon(image);
    }

    public ImageIcon createImageIcon(BufferedImage image, int width, int height) {
        return new ImageIcon(scaleImage(image, width, height));
    }

    public ImageIcon createImageIcon(BufferedImage image, int scale) {
        return new ImageIcon(scaleImage(image, scale));
    }

    public ImageIcon createImageIcon(String url) {
        return new ImageIcon(read(url));
    }

    public ImageIcon createImageIcon(String url, int width, int height) {
        return new ImageIcon(scaleImage(url, width, height));
    }

    public ImageIcon createImageIcon(String url, int scale) {
        return new ImageIcon(scaleImage(url, scale));
    }


    // Setter
    public void clearCache() {
        bufferedImageCache.clear();
    }

    public void clearCache(String resource) {
        bufferedImageCache.remove(resource);
    }

    // Getter
    public HashMap<String, BufferedImage> getBufferedImageCache() {
        return bufferedImageCache;
    }
}