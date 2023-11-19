package de.MCmoderSD.objects;

import de.MCmoderSD.main.Config;

import javax.swing.ImageIcon;
import java.awt.Point;
import java.awt.Color;
import java.util.Random;

@SuppressWarnings("unused")
public class Cat extends Point {

    // Attributes
    private final ImageIcon image;
    private final Color color;

    // Default Constructor
    public Cat(Config config) {
        Random random = new Random();
        int x = random.nextInt(config.getFieldSize());
        int y = random.nextInt(config.getFieldSize());
        setLocation(x, y);

        image = config.getCatImage();
        color = config.getCatColor();
    }

    // Constructor
    public Cat(Config config, int x, int y) {
        setLocation(x, y);

        image = config.getCatImage();
        color = config.getCatColor();
    }

    // Getter
    public ImageIcon getImage() {
        return image;
    }

    public Color getColor() {
        return color;
    }
}