package de.MCmoderSD.data;

import de.MCmoderSD.core.Controller;
import de.MCmoderSD.main.Config;
import de.MCmoderSD.utilities.Calculate;
import de.MCmoderSD.utilities.MySQL;

import java.awt.*;
import java.util.Objects;

public class Data {

    // Associations
    private final Controller controller;
    private final Config config;

    // Variables
    private final Point[] obstacles;
    private final Cat catPosition;
    private boolean isCatOnMove;

    // Constructor
    public Data(Controller controller, Config config) {
        this.controller = controller;
        this.config = config;
        config.setData(this);

        // Initialize variables
        isCatOnMove = true;
        catPosition = new Cat(config);
        obstacles = new Point[config.getTries()];

        // Update Loop
        new Thread(() -> {
            while (true) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(1000);
                    getEncodedData();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public void updateEncodedData() {
        MySQL mySQL = config.getMySQL();
        if (!mySQL.isConnected()) return;
        String encodedData = Calculate.encodeData(this, config);
        mySQL.updateEncodedData(encodedData);
    }

    public void getEncodedData() {
        MySQL mySQL = config.getMySQL();
        if (!mySQL.isConnected()) return;
        String encodedData = mySQL.getEncodedData();
        String oldEncodedData = Calculate.encodeData(this, config);
        if (encodedData == null || Objects.equals(encodedData, oldEncodedData)) return;
        decodeData(encodedData);
    }

    public void decodeData(String encodedData) {
        String[] parts = encodedData.split(";");

        isCatOnMove = Objects.equals(parts[2], "1");

        String[] catCords = parts[3].split(":");
        setCat(new Point(Integer.parseInt(catCords[0]), Integer.parseInt(catCords[1])));

        int obstaclesPlaced = parts.length-3;

        for (int i = 0; i < obstaclesPlaced; i++) {
            String[] obstacleCords = parts[4+i].split(":");
            int x = Integer.parseInt(obstacleCords[0]);
            int y = Integer.parseInt(obstacleCords[1]);
            obstacles[0] = new Point(x, y);
        }
        controller.updateGameState();
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
    }

    public void setObstacle(Point obstacle) {
        for (int i = 0; i < obstacles.length; i++) {
            if (obstacles[i] == null) {
                obstacles[i] = obstacle;
                break;
            }
        }
    }

    public void nextMove() {
        isCatOnMove = !isCatOnMove;
        updateEncodedData();
    }
}
