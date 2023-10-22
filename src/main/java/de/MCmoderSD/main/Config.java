package de.MCmoderSD.main;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.core.Controller;
import de.MCmoderSD.data.Data;
import de.MCmoderSD.utilities.ImageReader;
import de.MCmoderSD.utilities.JsonReader;

import java.awt.*;

@SuppressWarnings("unused")
public class Config {
    // Associations
    private final ImageReader imageReader;
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
    private final Image[] arrows;

    // Messages
    private final String invalidMove;
    private final String invalidObstacle;
    private final String catWon;
    private final String catcherWon;
    private final String catIsNotOnMove;
    private final String catcherIsNotOnMove;
    private final String triesLeft;
    private final String catIsOnMove;
    private final String catcherIsOnMove;
    private final String[] directions;

    // Constructor
    public Config(String[] args) {
        this.args = args;

        String language = "en";
        if (args.length != 0) language = args[0];

        // Read config
        JsonReader jsonReader = new JsonReader();
        JsonNode config = jsonReader.read("/config/default.json");
        JsonNode languageSet = jsonReader.read("/languages/" + language + ".json");


        // Constants
        width = config.get("width").asInt();
        height = config.get("height").asInt();
        fieldSize = config.get("fieldSize").asInt();
        tries = config.get("tries").asInt();
        isResizable = config.get("isResizable").asBoolean();


        imageReader = new ImageReader();

        // Arrows
        arrows = new Image[4];
        arrows[0] = imageReader.read(config.get("arrowUp").asText());
        arrows[1] = imageReader.read(config.get("arrowLeft").asText());
        arrows[2] = imageReader.read(config.get("arrowDown").asText());
        arrows[3] = imageReader.read(config.get("arrowRight").asText());

        // Directions
        directions = new String[4];
        directions[0] = languageSet.get("up").asText();
        directions[1] = languageSet.get("left").asText();
        directions[2] = languageSet.get("down").asText();
        directions[3] = languageSet.get("right").asText();

        // Messages
        title = languageSet.get("title").asText();
        invalidMove = languageSet.get("invalidMove").asText();
        invalidObstacle = languageSet.get("invalidObstacle").asText();
        catWon = languageSet.get("catWon").asText();
        catcherWon = languageSet.get("catcherWon").asText();
        catIsNotOnMove = languageSet.get("catIsNotOnMove").asText();
        catcherIsNotOnMove = languageSet.get("catcherIsNotOnMove").asText();
        triesLeft = languageSet.get("triesLeft").asText();
        catIsOnMove = languageSet.get("catIsOnMove").asText();
        catcherIsOnMove = languageSet.get("catcherIsOnMove").asText();


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
    public ImageReader getImageReader() {
        return imageReader;
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

    public Image[] getArrows() {
        return arrows;
    }

    public String getDirections(int index) {
        return directions[index];
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

    public String getCatIsNotOnMove() {
        return catIsNotOnMove;
    }

    public String getCatcherIsNotOnMove() {
        return catcherIsNotOnMove;
    }

    public String getTriesLeft() {
        return triesLeft;
    }

    public String getCatIsOnMove() {
        return catIsOnMove;
    }

    public String getCatcherIsOnMove() {
        return catcherIsOnMove;
    }
}
