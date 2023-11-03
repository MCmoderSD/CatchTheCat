package de.MCmoderSD.main;

import de.MCmoderSD.core.Controller;
import de.MCmoderSD.utilities.Calculate;

public class Main {
    public static boolean streamingMode;
    public static void main(String[] args) {
        streamingMode = !Calculate.doesFileExist("/config/default.json");

        if (!streamingMode) new Controller(new Config(args));
        else new Controller(new Config(args, "https://raw.githubusercontent.com/MCmoderSD/CatchTheCat/master/src/main/resources/config/streaming.json"));
    }
}
