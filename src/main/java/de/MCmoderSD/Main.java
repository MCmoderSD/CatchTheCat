package de.MCmoderSD;

public class Main {
    public static void main(String[] args) {
        Config config = new Config(args);
        new Controller(config);
    }
}
