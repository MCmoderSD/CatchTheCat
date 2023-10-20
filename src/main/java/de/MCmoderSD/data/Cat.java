package de.MCmoderSD.data;

import de.MCmoderSD.main.Config;

import java.awt.*;

public class Cat extends Point {
    public Cat(Config config) {
        super(config.getFieldSize() / 2, config.getFieldSize() / 2); // The Cat starts in the middle
    }
}
