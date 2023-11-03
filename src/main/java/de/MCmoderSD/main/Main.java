package de.MCmoderSD.main;

import de.MCmoderSD.core.Controller;
import de.MCmoderSD.utilities.Calculate;

public class Main {
    public static boolean steamingMode;
    public static void main(String[] args) {
        steamingMode = !Calculate.doesFileExist("/config/streaming.json");

        if (!steamingMode) new Controller(new Config(args));
        else new Controller(new Config(args, "https://raw.githubusercontent.com/MCmoderSD/Snake/master/src/main/resources/config/streaming.json"));
    }
}
