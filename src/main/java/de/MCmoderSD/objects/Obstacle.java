package de.MCmoderSD.objects;

import de.MCmoderSD.main.Config;

import javax.swing.ImageIcon;
import java.awt.Point;
import java.awt.Color;

@SuppressWarnings("unused")
public class Obstacle extends Point {

    // Attributes
    private final ImageIcon image;
    private final Color color;

    // Constructor
    public Obstacle(Config config, int x, int y) {
        setLocation(x, y);

        image = config.getObstacleImage();
        color = config.getObstacleColor();
    }

    // Getter
    public ImageIcon getImage() {
        return image;
    }

    public Color getColor() {
        return color;
    }
}
