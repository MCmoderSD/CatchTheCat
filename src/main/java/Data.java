import java.awt.*;

public class Data {
    // Associations
    private final Config config;

    // Variables
    private final Point[] obstacles;
    private Point catPosition;
    private boolean isCatOnMove;

    // Constructor
    public Data(Config config) {
        config.setData(this);
        this.config = config;

        isCatOnMove = true;
        catPosition = new Point(config.getFieldSize() / 2, config.getFieldSize() / 2);
        obstacles = new Point[config.getTries()];
    }

    // Getter
    public Point getCatPosition() {
        return catPosition;
    }

    public Point[] getObstacles() {
        return obstacles;
    }

    // Setter
    public void setCatPosition(Point catPosition) {
        this.catPosition = catPosition;
    }

    public void setObstacle(Point obstacle, int index) {
        obstacles[index] = obstacle;
    }
}
