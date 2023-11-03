package de.MCmoderSD.data;

import de.MCmoderSD.core.Controller;
import de.MCmoderSD.main.Config;
import de.MCmoderSD.utilities.Calculate;
import de.MCmoderSD.utilities.MySQL;

import java.awt.Point;
import java.util.Objects;

public class Data {

    // Associations
    private final Config config;

    // Variables
    private final Point[] obstacles;
    private final Cat catPosition;
    private boolean isCatOnMove;

    // Constructor
    @SuppressWarnings("BusyWait")
    public Data(Controller controller, Config config) {
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
                    boolean decodeIsValid;
                    String oldEncodedData;
                    String encodedData = null;
                    MySQL mySQL = config.getMySQL();

                    if (!mySQL.isConnected()) decodeIsValid = false;
                    else {
                        encodedData = mySQL.getEncodedData();
                        oldEncodedData = Calculate.encodeData(this, config);
                        decodeIsValid = config.getUI() != null && !Objects.equals(encodedData, oldEncodedData);
                    }

                    // Decode
                    if (decodeIsValid) {
                        String[] parts = encodedData.split(";");

                        isCatOnMove = Objects.equals(parts[2], "1");

                        String[] catCords = parts[3].split(":");
                        setCat(new Point(Integer.parseInt(catCords[0]), Integer.parseInt(catCords[1])));

                        if (parts.length > 4) {
                            for (int i = 0; i < parts.length - 4; i++) {
                                String[] obstacleCords = parts[4 + i].split(":");
                                int x = Integer.parseInt(obstacleCords[0]);
                                int y = Integer.parseInt(obstacleCords[1]);
                                obstacles[i] = new Point(x, y);
                            }
                        }
                        controller.updateGameState();
                        System.out.println("Decoded and Updated");
                    } else Thread.sleep(100);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    // Methods
    public void updateEncodedData() {
        MySQL mySQL = config.getMySQL();
        if (!mySQL.isConnected()) return;
        String encodedData = Calculate.encodeData(this, config);
        mySQL.updateEncodedData(encodedData);
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
        if (this.catPosition == catPosition) return; // No change
        this.catPosition.setLocation(catPosition); // Update
    }

    public void setObstacle(Point obstacle) {
        for (int i = 0; i < obstacles.length; i++) {
            // Find unused slot
            if (obstacles[i] == null) {
                obstacles[i] = obstacle;
                break;
            }
        }
    }

    public void nextMove() {
        isCatOnMove = !isCatOnMove; // Switch
        updateEncodedData(); // Update
    }
}
