import java.awt.*;
import java.awt.event.KeyEvent;

public class Controller {
    private final Config config;
    private final Data data;
    private final UI ui;
    public Controller(Config config) {
        this.config = config;
        config.setController(this);

        data = new Data(config);
        config.setData(data);

        ui = new UI(config);
    }

    // Methods
    private boolean isMoveValid(Point point) {
        if (point.equals(data.getCatPosition())) return false;
        for (Point obstacle : data.getObstacles()) if (point.equals(obstacle)) return false;

        int deltaX = (int) (point.getX() - data.getCatPosition().getX());
        int deltaY = (int) (point.getY() - data.getCatPosition().getY());
        int delta = Math.abs(deltaX) + Math.abs(deltaY);
        return delta == 1;
    }

    private boolean isObstacleValid(Point point) {
        if (point.equals(data.getCatPosition())) return false;

        int tries = config.getTries();
        for (Point obstacle : data.getObstacles()) {
            if (point.equals(obstacle)) return false;
            if (obstacle != null) --tries;
        }

        return tries > 0;
    }

    private void checkForWinner() {
        int tries = config.getTries();
        for (Point obstacle : data.getObstacles()) if (obstacle != null) --tries;

        int validMoves = 0;

        if (isMoveValid(new Point(data.getCatPosition().x - 1, data.getCatPosition().y))) ++validMoves;
        if (isMoveValid(new Point(data.getCatPosition().x + 1, data.getCatPosition().y))) ++validMoves;
        if (isMoveValid(new Point(data.getCatPosition().x, data.getCatPosition().y - 1))) ++validMoves;
        if (isMoveValid(new Point(data.getCatPosition().x, data.getCatPosition().y + 1))) ++validMoves;

        if (validMoves == 0) winner(false);
        else if (tries == 0) winner(true);
    }

    private void winner(boolean isCat) {
        if (isCat) ui.showMessage(config.getCatWon());
        else ui.showMessage(config.getCatcherWon());
        System.exit(0);
    }

    // Cat Movement
    public void catPlaysMove(int key) {

        int x = data.getCatPosition().x;
        int y = data.getCatPosition().y;

        switch (key) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                --y; break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                --x; break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                ++y; break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                ++x; break;
        }

        catPlaysMove(new Point(x, y));
    }

    public void catPlaysMove(Point point) {
        if (!isMoveValid(point)) ui.showMessage(config.getInvalidMove());
        else {
            ui.setButton(point, data.getCatPosition());
            data.setCatPosition(point);
        }
        checkForWinner();
    }

    // Obstacle Placement
    public void placeObstacle(Point point) {
        if (!isObstacleValid(point)) ui.showMessage(config.getInvalidObstacle());
        else {
            ui.setButton(point);
            data.setObstacle(point);
        }
        checkForWinner();
    }
}
