import java.awt.*;
import java.awt.event.KeyEvent;

public class Controller {
    // Associations
    private final Config config;
    private final Data data;
    private final UI ui;

    // Constructor
    public Controller(Config config) {
        this.config = config;
        config.setController(this);

        data = new Data(config);
        config.setData(data);

        ui = new UI(config);
    }

    // Methods

    // Check if move is valid
    private boolean isMoveValid(Point point) {
        if (point.equals(data.getCat())) return false; // Cat can't move to its own position
        for (Point obstacle : data.getObstacles()) if (point.equals(obstacle)) return false; // Cat can't move to an obstacle

        // Cat can't move diagonally or more than one tile
        int deltaX = (int) (point.getX() - data.getCat().getX());
        int deltaY = (int) (point.getY() - data.getCat().getY());
        int delta = Math.abs(deltaX) + Math.abs(deltaY);
        return delta == 1;
    }

    // Check if obstacle placement is valid
    private boolean isObstacleValid(Point point) {
        if (point.equals(data.getCat())) return false; // Obstacle can't be placed on cat's position

        // Check if an obstacle is already placed and if there are any tries left
        int tries = config.getTries();
        for (Point obstacle : data.getObstacles()) {
            if (point.equals(obstacle)) return false;
            if (obstacle != null) --tries;
        }

        return tries > 0;
    }

    // Check if somebody won
    private void checkForWinner() {

        // Check how many tries are left
        int tries = config.getTries();
        for (Point obstacle : data.getObstacles()) if (obstacle != null) --tries;

        // Check how many valid moves are left
        int validMoves = 0;

        if (isMoveValid(new Point(data.getCat().x - 1, data.getCat().y))) ++validMoves;
        if (isMoveValid(new Point(data.getCat().x + 1, data.getCat().y))) ++validMoves;
        if (isMoveValid(new Point(data.getCat().x, data.getCat().y - 1))) ++validMoves;
        if (isMoveValid(new Point(data.getCat().x, data.getCat().y + 1))) ++validMoves;

        // Check if somebody won
        if (validMoves == 0) winner(false);
        else if (tries == 0) winner(true);
    }

    // Winner
    private void winner(boolean isCat) {
        if (isCat) ui.showMessage(config.getCatWon()); // Cat won
        else ui.showMessage(config.getCatcherWon()); // Catcher won
        System.exit(0); // Exit the game
    }

    // Public Methods

    // Convert Key to Point and call catPlaysMove(Point)
    public void catPlaysMove(int key) {

        // Get cat's position
        int x = data.getCat().x;
        int y = data.getCat().y;

        // Convert key into an x or y direction
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

        // Calls catPlaysMove(Point) with the calculated point
        catPlaysMove(new Point(x, y));
    }

    // Cat Movement
    public void catPlaysMove(Point point) {
        // Check if move is valid
        if (!isMoveValid(point)) ui.showMessage(config.getInvalidMove()); // Invalid move
        else {
            ui.setButton(point, data.getCat()); // Update UI
            data.setCat(point); // Update data
        }
        checkForWinner(); // Check if somebody won
    }

    // Obstacle Placement
    public void placeObstacle(Point point) {
        // Check if obstacle placement is valid
        if (!isObstacleValid(point)) ui.showMessage(config.getInvalidObstacle()); // Invalid obstacle
        else {
            ui.setButton(point); // Update UI
            data.setObstacle(point); // Update data
        }
        checkForWinner(); // Check if somebody won
    }
}
