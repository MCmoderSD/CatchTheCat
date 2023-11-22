package de.MCmoderSD.main;

import de.MCmoderSD.UI.Frame;
import de.MCmoderSD.utilities.Calculate;

public class Main {
    public static void main(String[] args) {
        if (Calculate.doesFileExist("/config/default.json")) new Frame(new Config(args)); // Normal start
        else new Frame(new Config(args, "https://raw.githubusercontent.com/MCmoderSD/CatchTheCat/master/src/main/resources")); // Asset Streaming
    }
}
