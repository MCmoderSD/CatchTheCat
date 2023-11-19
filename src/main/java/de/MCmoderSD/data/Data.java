package de.MCmoderSD.data;

import de.MCmoderSD.core.Controller;
import de.MCmoderSD.main.Config;
import de.MCmoderSD.objects.Cat;
import de.MCmoderSD.objects.Obstacle;
import de.MCmoderSD.utilities.Calculate;
import de.MCmoderSD.utilities.database.MySQL;

import java.awt.Point;
import java.util.Objects;

public class Data {

    // Associations
    private final Config config;
    private final MySQL mySQL;

    // Variables
    private final Obstacle[] obstacles;
    private final Cat cat;
    private boolean isCatOnMove;

    // Constructor
    @SuppressWarnings("BusyWait")
    public Data(Controller controller, Config config) {
        this.config = config;
        this.mySQL = config.getMySQL();

        // Initialize variables
        isCatOnMove = true;
        cat = new Cat(config);
        obstacles = new Obstacle[config.getTries()];

        // Update Loop
        new Thread(() -> {
            while (true) {
                try {

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

                        isCatOnMove = Objects.equals(parts[2], "1");

                        String[] catCords = parts[3].split(":");
                        setCatLocation(new Point(Integer.parseInt(catCords[0]), Integer.parseInt(catCords[1])));

                        if (parts.length > 4) {
                            for (int i = 0; i < parts.length - 4; i++) {
                                String[] obstacleCords = parts[4 + i].split(":");
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
        }).start();
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
}
