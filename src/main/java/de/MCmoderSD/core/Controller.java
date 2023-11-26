package de.MCmoderSD.core;

import de.MCmoderSD.UI.Frame;
import de.MCmoderSD.data.Data;
import de.MCmoderSD.main.Config;
import de.MCmoderSD.main.Main;
import de.MCmoderSD.utilities.Calculate;
import de.MCmoderSD.utilities.database.MySQL;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class Controller {

    // Associations
    private final Config config;
    private final Frame frame;
    private final Data data;

    // Constructor
    public Controller(Frame frame, Config config) {
        this.frame = frame;
        this.config = config;

        data = new Data(this, config);

        updateGameState();
        frame.setVisible(true);
    }



    // Private Methods


    // Check if move is valid
    private boolean isMoveValid(Point point) {
        if (point.equals(data.getCat())) return false; // Cat can't move to its own position
        for (Point obstacle : data.getObstacles()) if (point.equals(obstacle)) return false; // Cat can't move onto an obstacle
        if (point.x < 0 || point.x >= config.getFieldSize() || point.y < 0 || point.y >= config.getFieldSize()) return false; // Cat can't move outside the field

        // Cat can't move diagonally or more than one tile
        int deltaX = (int) (point.getX() - data.getCat().getX());
        int deltaY = (int) (point.getY() - data.getCat().getY());
        return Math.abs(deltaX) + Math.abs(deltaY) == 1;
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

        // Check how many valid moves are left
        int validMoves = 0;

        if (isMoveValid(new Point(data.getCat().x - 1, data.getCat().y))) ++validMoves;
        if (isMoveValid(new Point(data.getCat().x + 1, data.getCat().y))) ++validMoves;
        if (isMoveValid(new Point(data.getCat().x, data.getCat().y - 1))) ++validMoves;
        if (isMoveValid(new Point(data.getCat().x, data.getCat().y + 1))) ++validMoves;

        // Check if somebody won
        if (validMoves == 0) winner(false);
        else if (Calculate.calculateTriesLeft(data, config) == 0) winner(true);
    }

    // Winner
    private void winner(boolean isCat) {
        if (isCat) { // Cat won
            frame.appendLog(config.getCatWon());
            frame.showMessage(config.getCatWon());
        }
        else { // Catcher won
            frame.appendLog(config.getCatcherWon());
            frame.showMessage(config.getCatcherWon());
        }

        // Update UI
        frame.hideMultiplayerComponents();
        frame.setRestartButtonVisible(true);
    }



    // Public Methods


    // Update UI
    public void updateGameState() {

        // Update Button Panel
        frame.setCat(data.getCat()); // Update cat
        for (Point obstacle : data.getObstacles()) if (obstacle != null) frame.setObstacle(obstacle); // Update obstacles

        // Update UI
        if (data.isCatOnMove()) frame.appendLog(config.getCatIsOnMove()); // Cat is on the move
        else frame.appendLog(config.getCatcherIsOnMove()); // Catcher is on the move

        // Update Game State
        checkForWinner(); // Check if somebody won
        frame.updateTries(Calculate.calculateTriesLeft(data, config)); // Update tries
    }

    // Convert Key to Point and call catPlaysMove (Point)
    public void catPlaysMove(int key) {
        if (!data.isCatOnMove()) frame.showMessage(config.getCatIsNotOnMove()); // Cat is not on the move
        else {
            // Get cat's position
            int x = data.getCat().x;
            int y = data.getCat().y;

            // Convert key into an x or y direction
            switch (key) {
                case 0x57:  // W Key
                case 0x26:  // Up Arrow
                case 0:     // Button up
                    --y;    // Move up
                    break;
                case 0x41:  // A Key
                case 0x25:  // Left Arrow
                case 1:     // Button left
                    --x;    // Move left
                    break;
                case 0x53:  // S Key
                case 0x28:  // Down Arrow
                case 2:     // Button down
                    ++y;    // Move down
                    break;
                case 0x44:  // D Key
                case 0x27:  // Right Arrow
                case 3:     // Button right
                    ++x;    // Move right
                    break;
            }

            // Calls catPlaysMove(Point) with the calculated point
            catPlaysMove(new Point(x, y));
        }
    }

    // Cat Movement
    public void catPlaysMove(Point point) {
        if (!data.isCatOnMove()) frame.showMessage(config.getCatIsNotOnMove()); // Cat is not on the move
        else {
            // Check if move is valid
            if (!isMoveValid(point)) frame.showMessage(config.getInvalidMove()); // Invalid move
            else {
                data.setCatLocation(point); // Update data
                data.nextMove(); // Switch
                updateGameState(); // Update UI
            }
        }
    }

    // Obstacle Placement
    public void placeObstacle(Point point) {
        if (data.isCatOnMove()) catPlaysMove(point); // Cat is on the move

        // Check if obstacle placement is valid
        else {
            if (!isObstacleValid(point)) frame.showMessage(config.getInvalidObstacle()); // Invalid obstacle
            else {
                data.setObstacle(point); // Update data
                data.nextMove(); // Switch
                updateGameState(); // Update UI
            }
        }
    }

    // Host Game
    public void hostGame() {

        // Create new Game on MySQL Server
        String gameID = Calculate.generateRandomID();
        MySQL mySQL = config.getMySQL();
        mySQL.setGameID(gameID);
        mySQL.connect();

        // Update encoded data
        data.pushDataToMySQL();

        // Copy To Clipboard
        StringSelection stringSelection = new StringSelection(gameID);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);

        // Update UI
        frame.appendLog(config.getRoomID() + gameID);
        frame.hideMultiplayerComponents();
    }

    // Join Game
    public void joinGame(String input) {
        StringBuilder roomID = new StringBuilder();

        // Filter out all non-digits and non-letters
        char[] inputChars = input.toCharArray();
        for (char inputChar : inputChars) if (Character.isDigit(inputChar) || Character.isLetter(inputChar)) roomID.append(inputChar);

        // Valid roomID
        if (roomID.length() == 6) {
            frame.setVisible(false); // Hide old frame
            Main.main(new String[] {config.getLanguage(), roomID.toString()}); // Start new game
        }
    }

    // Restart Game
    public void restartGame() {
        data.toggleNewGame(!data.isNewGame()); // Switch
        data.initData(); // Reset data
        frame.clearLog(); // Clear log
        frame.setRestartButtonVisible(false); // Hide restart button
    }
}