package de.MCmoderSD.main;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.UI.UI;
import de.MCmoderSD.core.Controller;
import de.MCmoderSD.data.Data;
import de.MCmoderSD.utilities.Calculate;
import de.MCmoderSD.utilities.ImageReader;
import de.MCmoderSD.utilities.JsonReader;
import de.MCmoderSD.utilities.MySQL;

import java.awt.*;

@SuppressWarnings("unused")
public class Config {

    // Associations
    private final ImageReader imageReader;
    private final MySQL mySQL;
    private Controller controller;
    private Data data;
    private UI ui;

    // Constants
    private final String[] args;
    private final String language;
    private final String gameID;
    private final int width;
    private final int height;
    private final Dimension dimension;
    private final int fieldSize;
    private final int tries;
    private final boolean isResizable;
    private final Image[] arrows;

    // Language
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
        else language = args[0].toLowerCase();

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

        mySQL.connect();

        // Constants
        JsonNode config = jsonReader.read("/config/default.json");
        width = config.get("width").asInt();
        height = config.get("height").asInt();
        isResizable = config.get("isResizable").asBoolean();
        dimension = new Dimension(width, height);
        imageReader = new ImageReader();

        // Field Size and Tries
        String[] databaseArgs = null;
        if (gameID != null) databaseArgs = Calculate.decodeData(mySQL.getEncodedData());

        if (databaseArgs == null) {
            fieldSize = config.get("fieldSize").asInt();
            tries = config.get("tries").asInt();
        } else {
            fieldSize = Integer.parseInt(databaseArgs[0]);
            tries = Integer.parseInt(databaseArgs[1]);
        }

        // Arrows
        arrows = new Image[4];
        arrows[0] = imageReader.read(config.get("arrowUp").asText());
        arrows[1] = imageReader.read(config.get("arrowLeft").asText());
        arrows[2] = imageReader.read(config.get("arrowDown").asText());
        arrows[3] = imageReader.read(config.get("arrowRight").asText());

        // Directions
        JsonNode languageSet = jsonReader.read("/languages/" + language + ".json");
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
        host = languageSet.get("host").asText();
        join = languageSet.get("join").asText();
        roomID = languageSet.get("roomID").asText();
    }

    // Setter Associations
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setUI(UI ui) {
        this.ui = ui;
    }

    // Getter Associations
    public ImageReader getImageReader() {
        return imageReader;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public Controller getController() {
        return controller;
    }

    public Data getData() {
        return data;
    }

    public UI getUI() {
        return ui;
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
