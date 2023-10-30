package de.MCmoderSD.data;

import de.MCmoderSD.main.Config;

import java.awt.*;
import java.util.Random;

public class Cat extends Point {

    public Cat(Config config) {
        Random random = new Random();
        int x = random.nextInt(config.getFieldSize());
        int y = random.nextInt(config.getFieldSize());
        setLocation(x, y);
    }
}