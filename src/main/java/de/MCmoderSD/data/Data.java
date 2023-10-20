package de.MCmoderSD.data;

import de.MCmoderSD.main.Config;

import java.awt.*;

public class Data {

    // Variables
    private final Point[] obstacles;
    private final Cat catPosition;
    private boolean isCatOnMove;

    // Constructor
    public Data(Config config) {
        config.setData(this);

        // Initialize variables
        isCatOnMove = true;
        catPosition = new Cat(config);
        obstacles = new Point[config.getTries()];
    }

    // Getter
    public Point getCat() {
        return catPosition;
    }

    public Point[] getObstacles() {
        return obstacles;
    }

    public boolean isCatOnMove() {
        return isCatOnMove;
    }

    // Setter
    public void setCat(Point catPosition) {
        if (this.catPosition == catPosition) return;
        this.catPosition.setLocation(catPosition);
        isCatOnMove = !isCatOnMove; // Switch turns
    }

    public void setObstacle(Point obstacle) {
        for (int i = 0; i < obstacles.length; i++) {
            if (obstacles[i] == null) {
                obstacles[i] = obstacle;
                break;
            }
        }
        isCatOnMove = !isCatOnMove; // Switch turns
    }
}
