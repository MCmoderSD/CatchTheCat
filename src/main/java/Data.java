import java.awt.*;

public class Data {

    // Variables
    private final Point[] obstacles;
    private final Cat catPosition;
    private boolean isCatOnMove;

    // Constructor
    public Data(Config config) {
        config.setData(this);

        isCatOnMove = true;
        catPosition = new Cat(config);
        obstacles = new Point[config.getTries()];
    }

    // Getter
    public Point getCatPosition() {
        return catPosition;
    }

    public Point[] getObstacles() {
        return obstacles;
    }

    public boolean isCatOnMove() {
        return isCatOnMove;
    }

    // Setter
    public void setCatPosition(Point catPosition) {
        if (this.catPosition == catPosition) return;
        this.catPosition.setLocation(catPosition);
        isCatOnMove = !isCatOnMove;
    }

    public void setObstacle(Point obstacle) {
        for (int i = 0; i < obstacles.length; i++) {
            if (obstacles[i] == null) {
                obstacles[i] = obstacle;
                break;
            }
        }
        isCatOnMove = !isCatOnMove;
    }
}
