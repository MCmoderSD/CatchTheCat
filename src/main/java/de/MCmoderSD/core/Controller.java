package de.MCmoderSD.core;

import de.MCmoderSD.UI.UI;
import de.MCmoderSD.data.Data;
import de.MCmoderSD.main.Config;
import de.MCmoderSD.utilities.Calculate;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Controller {
    // Associations
    private final Config config;
    private final Data data;
    private final UI ui;
    private String encodedData;

    // Constructor
    public Controller(Config config) {
        this.config = config;
        config.setController(this);

        data = new Data(config);
        config.setData(data);

        new Thread(() -> {
            while (true) {
                if (config.getMySQL().isConnected()) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                    }
                    updateGameState();
                }
            }
        }).start();
        ui = new UI(config);

        // Debug
        new Thread(() -> {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                if (Objects.equals(input, "encode")) {
                    encodedData = Calculate.encodeData(data, config);
                    System.out.println(encodedData);
                    data.decodeData(encodedData);
                    System.out.println(Calculate.generateRandomID());
                }
            }
        }).start();
    }

    // Methods


    // Check if move is valid
    private boolean isMoveValid(Point point) {
        if (point.equals(data.getCat())) return false; // Cat can't move to its own position
        for (Point obstacle : data.getObstacles()) if (point.equals(obstacle)) return false; // Cat can't move to an obstacle
        if (point.x < 0 || point.x >= config.getFieldSize() || point.y < 0 || point.y >= config.getFieldSize()) return false; // Cat can't move outside the field

        // Cat can't move diagonally or more than one tile
        int deltaX = (int) (point.getX() - data.getCat().getX());
        int deltaY = (int) (point.getY() - data.getCat().getY());
        int delta = Math.abs(deltaX) + Math.abs(deltaY);
        return delta == 1;
    }

    // Check if obstacle placement is valid
    private boolean isObstacleValid(Point point) {
        if (point.equals(data.getCat())) return false; // Obstacle can't be placed on cat's position
        for (Point obstacle : data.getObstacles()) if (point.equals(obstacle)) return false; // Obstacle can't be placed on another obstacle

        // Check if an obstacle is already placed and if there are any tries left
        return Calculate.calculateTriesLeft(data, config) > 0;
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
        if (isCat) { // Cat won
            ui.appendLog(config.getCatWon());
            ui.showMessage(config.getCatWon());
        }
        else { // Catcher won
            ui.appendLog(config.getCatcherWon());
            ui.showMessage(config.getCatcherWon());
        }
    }

    private void updateGameState() {
        ui.setCat(data.getCat()); // Update UI
        for (Point obstacle : data.getObstacles()) if (obstacle != null) ui.setObstacle(obstacle); // Update UI
    }

    // Public Methods

    // Convert Key to Point and call catPlaysMove(Point)
    public void catPlaysMove(int key) {
        if (!data.isCatOnMove()) ui.showMessage(config.getCatIsNotOnMove()); // Cat is not on the move
        else {
            // Get cat's position
            int x = data.getCat().x;
            int y = data.getCat().y;

            // Convert key into an x or y direction
            switch (key) {
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                case 0:
                    --y;
                    break;
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:
                case 1:
                    --x;
                    break;
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                case 2:

                    ++y;
                    break;
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT:
                case 3:
                    ++x;
                    break;
            }

            // Calls catPlaysMove(Point) with the calculated point
            catPlaysMove(new Point(x, y));
        }
    }

    // Cat Movement
    public void catPlaysMove(Point point) {
        if (!data.isCatOnMove()) ui.showMessage(config.getCatIsNotOnMove()); // Cat is not on the move
        else {
            // Check if move is valid
            if (!isMoveValid(point)) ui.showMessage(config.getInvalidMove()); // Invalid move
            else {
                data.setCat(point); // Update data
                data.nextMove(); // Next move
                ui.appendLog(config.getCatcherIsOnMove());
                updateGameState();
            }
            checkForWinner(); // Check if somebody won
        }
    }

    // Obstacle Placement
    public void placeObstacle(Point point) {
        if (data.isCatOnMove()) catPlaysMove(point);
        else {
        // Check if obstacle placement is valid
        if (!isObstacleValid(point)) ui.showMessage(config.getInvalidObstacle()); // Invalid obstacle
        else {
            data.setObstacle(point); // Update data
            data.nextMove(); // Next move
            ui.appendLog(config.getCatIsOnMove());
            updateGameState();
        }

        checkForWinner(); // Check if somebody won
        ui.updateTries(Calculate.calculateTriesLeft(data, config)); // Update tries
        }
    }

    // Host Game
    public void hostGame() {
        String gameID = Calculate.generateRandomID();
        config.getMySQL().setGameID(gameID);
        config.getMySQL().connect();
        data.updateEncodedData();

        ui.appendLog(config.getRoomID() + gameID);
        ui.hideHostButton();
    }
}