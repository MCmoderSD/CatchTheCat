import com.fasterxml.jackson.databind.JsonNode;

import java.awt.*;

public class Config {
    // Associations
    private final Utils utils;
    private Controller controller;
    private UI ui;
    private Data data;
    private Cat cat;

    // Constants
    private final String[] args;
    private final String title;
    private final int width;
    private final int height;
    private final Dimension dimension;
    private final int fieldSize;
    private final boolean isResizable;
    private final int tries;

    // Constructor
    public Config(String[] args) {
        utils = new Utils();
        this.args = args;

        JsonNode config = utils.readJson("default");

        title = config.get("title").asText();
        width = config.get("width").asInt();
        height = config.get("height").asInt();
        fieldSize = config.get("fieldSize").asInt();
        isResizable = config.get("isResizable").asBoolean();
        tries = config.get("tries").asInt();

        dimension = new Dimension(width, height);
    }

    // Setter

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setUI(UI ui) {
        this.ui = ui;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }


    // Getter Associations
    public Utils getUtils() {
        return utils;
    }

    public Controller getController() {
        return controller;
    }

    public UI getUi() {
        return ui;
    }

    public Data getData() {
        return data;
    }

    public Cat getCat() {
        return cat;
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

    public boolean isResizable() {
        return isResizable;
    }

    public int getTries() {
        return tries;
    }
}
