package de.MCmoderSD.main;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.utilities.Calculate;
import de.MCmoderSD.utilities.database.MySQL;
import de.MCmoderSD.utilities.image.ImageReader;
import de.MCmoderSD.utilities.image.ImageStreamer;
import de.MCmoderSD.utilities.json.JsonReader;
import de.MCmoderSD.utilities.json.JsonStreamer;

import javax.swing.ImageIcon;
import java.awt.Color;


@SuppressWarnings("unused")
public class Config {

    // Associations
    private final MySQL mySQL;
    private final ImageReader imageReader;
    private final ImageStreamer imageStreamer;

    // Constants
    private final String[] args;
    private final String language;
    private final String gameID;
    private final int width;
    private final int height;
    private final int fieldSize;
    private final int tries;
    private final boolean isResizable;

    // Assets
    private final ImageIcon[] arrows;
    private final ImageIcon catImage;
    private final ImageIcon obstacleImage;
    private final Color catColor;
    private final Color obstacleColor;
    private final Color backgroundColor;
    private final Color fontColor;

    // Messages
    private final String title;
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
    private final String host;
    private final String join;
    private final String roomID;


    // Constructor
    public Config(String[] args) {
        this.args = args;

        // Language
        if (args.length == 0) language = "en";
        else language = args[0];

        // GameID
        if (args.length == 2) gameID = args[1];
        else gameID = null;

        // Read Config
        JsonReader jsonReader = new JsonReader();
        JsonNode database = jsonReader.read("/config/database.json");

        // Initialize MySQL Connection
        mySQL = new MySQL(

                database.get("host").asText(),
                database.get("port").asInt(),
                database.get("database").asText(),
                database.get("user").asText(),
                database.get("password").asText(),
                database.get("table").asText(),
                gameID);


        JsonNode config = jsonReader.read("/config/default.json");

        // Constants
        width = config.get("width").asInt();
        height = config.get("height").asInt();
        isResizable = config.get("isResizable").asBoolean();

        // Field Size and Tries
        String[] databaseArgs = null;
        if (gameID != null) databaseArgs = Calculate.decodeData(mySQL.pullFromMySQL());

        if (databaseArgs == null) {
            fieldSize = config.get("fieldSize").asInt();
            tries = config.get("tries").asInt();
        } else {
            fieldSize = Integer.parseInt(databaseArgs[0]);
            tries = Integer.parseInt(databaseArgs[1]);
        }

        imageReader = new ImageReader();
        imageStreamer = null;

        // Assets
        catImage = imageReader.createImageIcon(config.get("catImage").asText());
        obstacleImage = imageReader.createImageIcon(config.get("obstacleImage").asText());
        catColor = Calculate.getColor(config.get("catColor").asText());
        obstacleColor = Calculate.getColor(config.get("obstacleColor").asText());
        backgroundColor = Calculate.getColor(config.get("backgroundColor").asText());
        fontColor = Calculate.getColor(config.get("fontColor").asText());

        // Arrows
        arrows = new ImageIcon[4];
        arrows[0] = imageReader.createImageIcon(config.get("arrowUp").asText());
        arrows[1] = imageReader.createImageIcon(config.get("arrowLeft").asText());
        arrows[2] = imageReader.createImageIcon(config.get("arrowDown").asText());
        arrows[3] = imageReader.createImageIcon(config.get("arrowRight").asText());


        JsonNode languageSet = jsonReader.read("/languages/" + language + ".json");

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
        host = languageSet.get("host").asText();
        join = languageSet.get("join").asText();
        roomID = languageSet.get("roomID").asText();

        // Directions
        directions = new String[4];
        directions[0] = languageSet.get("up").asText();
        directions[1] = languageSet.get("left").asText();
        directions[2] = languageSet.get("down").asText();
        directions[3] = languageSet.get("right").asText();
    }

    // Constructor asset streaming
    public Config(String[] args, String url) {
        this.args = args;

        // Language
        if (args.length == 0) language = "en";
        else language = args[0];

        // GameID
        if (args.length == 2) gameID = args[1];
        else gameID = null;

        // Read Config
        JsonStreamer jsonStreamer = new JsonStreamer(url);
        JsonNode database = jsonStreamer.read("/config/database.json");

        // Initialize MySQL Connection
        mySQL = new MySQL(

                database.get("host").asText(),
                database.get("port").asInt(),
                database.get("database").asText(),
                database.get("user").asText(),
                database.get("password").asText(),
                database.get("table").asText(),
                gameID);


        JsonNode config = jsonStreamer.read("/config/default.json");

        // Constants
        width = config.get("width").asInt();
        height = config.get("height").asInt();
        isResizable = config.get("isResizable").asBoolean();

        // Field Size and Tries
        String[] databaseArgs = null;
        if (gameID != null) databaseArgs = Calculate.decodeData(mySQL.pullFromMySQL());

        if (databaseArgs == null) {
            fieldSize = config.get("fieldSize").asInt();
            tries = config.get("tries").asInt();
        } else {
            fieldSize = Integer.parseInt(databaseArgs[0]);
            tries = Integer.parseInt(databaseArgs[1]);
        }

        imageStreamer = new ImageStreamer(url);
        imageReader = null;

        // Assets
        catImage = imageStreamer.createImageIcon(config.get("catImage").asText());
        obstacleImage = imageStreamer.createImageIcon(config.get("obstacleImage").asText());
        catColor = Calculate.getColor(config.get("catColor").asText());
        obstacleColor = Calculate.getColor(config.get("obstacleColor").asText());
        backgroundColor = Calculate.getColor(config.get("backgroundColor").asText());
        fontColor = Calculate.getColor(config.get("fontColor").asText());

        // Arrows
        arrows = new ImageIcon[4];
        arrows[0] = imageStreamer.createImageIcon(config.get("arrowUp").asText());
        arrows[1] = imageStreamer.createImageIcon(config.get("arrowLeft").asText());
        arrows[2] = imageStreamer.createImageIcon(config.get("arrowDown").asText());
        arrows[3] = imageStreamer.createImageIcon(config.get("arrowRight").asText());


        JsonNode languageSet = jsonStreamer.read("/languages/" + language + ".json");

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
        host = languageSet.get("host").asText();
        join = languageSet.get("join").asText();
        roomID = languageSet.get("roomID").asText();

        // Directions
        directions = new String[4];
        directions[0] = languageSet.get("up").asText();
        directions[1] = languageSet.get("left").asText();
        directions[2] = languageSet.get("down").asText();
        directions[3] = languageSet.get("right").asText();
    }

    // Getter Associations
    public MySQL getMySQL() {
        return mySQL;
    }

    public ImageReader getImageReader() {
        return imageReader;
    }

    public ImageStreamer getImageStreamer() {
        return imageStreamer;
    }

    // Getter Constants
    public String[] getArgs() {
        return args;
    }

    public String getLanguage() {
        return language;
    }

    public String getGameID() {
        return gameID;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

    // Getter Assets
    public ImageIcon[] getArrows() {
        return arrows;
    }

    public ImageIcon getCatImage() {
        return catImage;
    }

    public ImageIcon getObstacleImage() {
        return obstacleImage;
    }

    public Color getCatColor() {
        return catColor;
    }

    public Color getObstacleColor() {
        return obstacleColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getFontColor() {
        return fontColor;
    }

    // Getter Messages
    public String getTitle() {
        return title;
    }

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

    public String getDirections(int index) {
        return directions[index];
    }

    public String getHost() {
        return host;
    }

    public String getJoin() {
        return join;
    }

    public String getRoomID() {
        return roomID;
    }
}
