import com.fasterxml.jackson.databind.JsonNode;

import java.awt.*;

@SuppressWarnings("unused")
public class Config {
    // Associations
    private final Utils utils;
    private Controller controller;
    private Data data;

    // Constants
    private final String[] args;
    private final String title;
    private final int width;
    private final int height;
    private final Dimension dimension;
    private final int fieldSize;
    private final int tries;
    private final boolean isResizable;

    // Messages
    private final String invalidMove;
    private final String invalidObstacle;
    private final String catWon;
    private final String catcherWon;

    // Constructor
    public Config(String[] args) {
        utils = new Utils();
        this.args = args;

        // Read config
        JsonNode config = utils.readJson("default");

        // Constants
        title = config.get("title").asText();
        width = config.get("width").asInt();
        height = config.get("height").asInt();
        fieldSize = config.get("fieldSize").asInt();
        tries = config.get("tries").asInt();
        isResizable = config.get("isResizable").asBoolean();

        // Messages
        invalidMove = config.get("invalidMove").asText();
        invalidObstacle = config.get("invalidObstacle").asText();
        catWon = config.get("catWon").asText();
        catcherWon = config.get("catcherWon").asText();

        dimension = new Dimension(width, height);
    }

    // Setter
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setData(Data data) {
        this.data = data;
    }


    // Getter Associations
    public Utils getUtils() {
        return utils;
    }

    public Controller getController() {
        return controller;
    }

    public Data getData() {
        return data;
    }


    // Getter Constants
    public String[] getArgs() {
        return args;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public int getTries() {
        return tries;
    }

    public boolean isResizable() {
        return isResizable;
    }


    // Getter Messages
    public String getCatcherWon() {
        return catcherWon;
    }

    public String getCatWon() {
        return catWon;
    }

    public String getInvalidMove() {
        return invalidMove;
    }

    public String getInvalidObstacle() {
        return invalidObstacle;
    }
}
