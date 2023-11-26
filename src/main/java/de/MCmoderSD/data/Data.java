package de.MCmoderSD.data;

import de.MCmoderSD.core.Controller;
import de.MCmoderSD.main.Config;
import de.MCmoderSD.objects.Cat;
import de.MCmoderSD.objects.Obstacle;
import de.MCmoderSD.utilities.Calculate;
import de.MCmoderSD.utilities.database.MySQL;

import java.awt.Point;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Data {

    // Associations
    private final Controller controller;
    private final Config config;
    private final MySQL mySQL;

    // Variables
    private Obstacle[] obstacles;
    private Cat cat;
    private boolean isCatOnMove;
    private boolean isNewGame;
    private boolean updateLoopActive;

    // Constructor
    public Data(Controller controller, Config config) {
        this.controller = controller;
        this.config = config;
        this.mySQL = config.getMySQL();

        initData();

        // Update Loop
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::updateFromMySQL, 0, 100, TimeUnit.MILLISECONDS);
        updateLoopActive = true;
    }

    // Pull data from MySQL
    private void updateFromMySQL() {
        if (!updateLoopActive) return; // Check if update loop is active

        // Variables
        boolean decodeIsValid;
        String oldEncodedData;
        String encodedData = null;

        // Pull and differentiate between local and online
        if (mySQL.isConnected()) {
            encodedData = mySQL.pullFromMySQL();
            oldEncodedData = Calculate.encodeData(this, config);
            decodeIsValid = !Objects.equals(encodedData, oldEncodedData);
        } else decodeIsValid = false;

        // Decode if valid
        if (decodeIsValid) {
            String[] parts = encodedData.split(";"); // Split on each semicolon

            // Decode if new game
            isNewGame = Objects.equals(parts[2], "1"); // Check if new game
            if (isNewGame) controller.restartGame(); // Restart if new game

            // Decode if cat is on move
            isCatOnMove = Objects.equals(parts[3], "1"); // Check if cat is on move

            // Decode cat
            String[] catCords = parts[4].split(":"); // Split cat cords into x and y
            setCatLocation(new Point(Integer.parseInt(catCords[0]), Integer.parseInt(catCords[1]))); // Set cat location

            // Decode obstacles
            if (parts.length > 5) {
                for (int i = 0; i < parts.length - 5; i++) { // For each placed obstacle
                    String[] obstacleCords = parts[5 + i].split(":"); // Split obstacle cords into x and y
                    int x = Integer.parseInt(obstacleCords[0]);
                    int y = Integer.parseInt(obstacleCords[1]);
                    obstacles[i] = new Obstacle(config, x, y); // Create new obstacle
                }
            }

            // Update game state
            controller.updateGameState();
            System.out.println("Decoded and Updated");
        }
    }

    // Push data to MySQL
    public void pushDataToMySQL() {
        mySQL.pushToMySQL(Calculate.encodeData(this, config));
    }

    // Setter
    public void initData() {

        // Reset data
        isCatOnMove = true;
        cat = new Cat(config);
        obstacles = new Obstacle[config.getTries()];

        // Update server
        pushDataToMySQL();
    }

    public void setCatLocation(Point catLocation) {
        if (cat.getLocation() == catLocation) return; // No change
        cat.setLocation(catLocation); // Update
    }

    public void setObstacle(Point obstacle) {
        for (int i = 0; i < obstacles.length; i++) {
            // Find unused slot
            if (obstacles[i] == null) {
                obstacles[i] = new Obstacle(config, obstacle.x, obstacle.y); // Create new obstacle
                break;
            }
        }
    }

    public void nextMove() {
        isCatOnMove = !isCatOnMove; // Switch
        pushDataToMySQL(); // Update
    }

    public void toggleNewGame(boolean newGame) {
        isNewGame = newGame;
    }

    public void toggleUpdateLoop(boolean updateLoopActive) {
        this.updateLoopActive = updateLoopActive;
    }

    // Getter
    public Cat getCat() {
        return cat;
    }

    public Obstacle[] getObstacles() {
        return obstacles;
    }

    public boolean isCatOnMove() {
        return isCatOnMove;
    }

    public boolean isNewGame() {
        return isNewGame;
    }

    public boolean isUpdateLoopActive() {
        return updateLoopActive;
    }
}
