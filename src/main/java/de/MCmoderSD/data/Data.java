package de.MCmoderSD.data;

import de.MCmoderSD.core.Controller;
import de.MCmoderSD.main.Config;
import de.MCmoderSD.objects.Cat;
import de.MCmoderSD.objects.Obstacle;
import de.MCmoderSD.utilities.Calculate;
import de.MCmoderSD.utilities.database.MySQL;

import java.awt.Point;
import java.util.Objects;

@SuppressWarnings("BusyWait")
public class Data {

    // Associations
    private final Controller controller;
    private final Config config;
    private final MySQL mySQL;

    // Variables
    private Thread updateLoop;
    private Obstacle[] obstacles;
    private Cat cat;
    private boolean isCatOnMove;
    private boolean isNewGame;
    private boolean updateThreadActive;

    // Constructor
    public Data(Controller controller, Config config) {
        this.controller = controller;
        this.config = config;
        this.mySQL = config.getMySQL();

        initData();
        updateLoop.start();
        updateThreadActive = true;
    }

    // Push data to MySQL
    public void pushDataToMySQL() {
        mySQL.pushToMySQL(Calculate.encodeData(this, config));
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

    // Setter
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

    public void initData() {
        isCatOnMove = true;
        cat = new Cat(config);
        obstacles = new Obstacle[config.getTries()];
        pushDataToMySQL();

        // Update Loop
        updateLoop = new Thread(() -> {
            while (true) {

                try {
                    if (!updateThreadActive) return;
                    boolean decodeIsValid;
                    String oldEncodedData;
                    String encodedData = null;

                    // Pull and differentiate between local and online
                    if (mySQL.isConnected()) {
                        encodedData = mySQL.pullFromMySQL();
                        oldEncodedData = Calculate.encodeData(this, config);
                        decodeIsValid = !Objects.equals(encodedData, oldEncodedData);
                    } else decodeIsValid = false;

                    // Decode
                    if (decodeIsValid) {
                        String[] parts = encodedData.split(";");

                        if (Objects.equals(parts[2], "1")) controller.restartGame();
                        isCatOnMove = Objects.equals(parts[3], "1");

                        String[] catCords = parts[4].split(":");
                        setCatLocation(new Point(Integer.parseInt(catCords[0]), Integer.parseInt(catCords[1])));

                        if (parts.length > 5) {
                            for (int i = 0; i < parts.length - 5; i++) {
                                String[] obstacleCords = parts[5 + i].split(":");
                                int x = Integer.parseInt(obstacleCords[0]);
                                int y = Integer.parseInt(obstacleCords[1]);
                                obstacles[i] = new Obstacle(config, x, y);
                            }
                        }
                        controller.updateGameState();
                        System.out.println("Decoded and Updated");
                    } else Thread.sleep(100); // Wait for 100ms
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        });
    }

    public void setNewGame(boolean newGame) {
        isNewGame = newGame;
    }

    public void setUpdateThreadActive(boolean updateThreadActive) {
        this.updateThreadActive = updateThreadActive;
    }
}
