package de.MCmoderSD.utilities;

import de.MCmoderSD.data.Data;
import de.MCmoderSD.main.Config;
import de.MCmoderSD.main.Main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.security.SecureRandom;

@SuppressWarnings("unused")
public class Calculate {

    // Center JFrame
    public static Point centerFrame(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Screen Size
        int x = ((screenSize.width - frame.getWidth()) / 2);
        int y = ((screenSize.height - frame.getHeight()) / 2);
        return new Point(x, y);
    }

    // Calculate average color in rectangle
    public static Color getAverageColorInRectangle(Rectangle rectangle, JPanel panel) {
        BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
        panel.paint(image.getGraphics());

        int startX = rectangle.x;
        int startY = rectangle.y;
        int endX = rectangle.x + rectangle.width;
        int endY = rectangle.y + rectangle.height;

        int totalRed = 0;
        int totalGreen = 0;
        int totalBlue = 0;
        int pixelCount = 0;

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                totalRed += red;
                totalGreen += green;
                totalBlue += blue;
                pixelCount++;
            }
        }

        int averageRed = totalRed / pixelCount;
        int averageGreen = totalGreen / pixelCount;
        int averageBlue = totalBlue / pixelCount;

        return new Color(averageRed, averageGreen, averageBlue);
    }

    // Calculate foreground color
    public static Color calculateForegroundColor(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        if (r + g + b > 382) return Color.BLACK;
        else return Color.WHITE;
    }

    // Calculate tries left
    public static int calculateTriesLeft(Data data, Config config) {
        int triesLeft = config.getTries();
        Point[] obstacles = data.getObstacles();
        for (Point obstacle : obstacles) if (obstacle != null) --triesLeft;
        return triesLeft;
    }

    // Encoder
    public static String encodeData(Data data, Config config) {
        String end = ";";
        int tries = config.getTries();
        int triesLeft = calculateTriesLeft(data, config);
        int isNewGame = data.isNewGame() ? 1 : 0;

        // Encode Config
        String confPack = config.getFieldSize() + end + tries + end + isNewGame;

        // Encode Data
        String dataPack;
        String[] obstaclePackTemp = new String[tries];

        // Encode CatIsOnMove
        int isCatOnMove = data.isCatOnMove() ? 1 : 0;

        // Encode Obstacles
        Point[] obstacles = data.getObstacles();
        for (int i = 0; i < tries; i++) if (obstacles[i] != null) obstaclePackTemp[i] = String.valueOf(obstacles[i].x) + ':' + obstacles[i].y;

        // Delete Null Values
        String[] obstaclePack = new String[tries-triesLeft];
        for (int i = 0; i < obstaclePack.length; i++) if (obstaclePackTemp[i] != null) obstaclePack[i] = obstaclePackTemp[i];

        dataPack = isCatOnMove + end + data.getCat().x + ':' + data.getCat().y + end + String.join(end, obstaclePack);

        // Finalize Package
        return confPack + end + dataPack;
    }

    // Decoder
    public static String[] decodeData(String encodedData) {
        String[] parts = encodedData.split(";");

        String[] result = new String[3];
        result[0] = parts[0];
        result[1] = parts[1];

        StringBuilder rest = new StringBuilder();
        for (int i = 2; i < parts.length; i++) {
            if (i != 2) rest.append(";");
            rest.append(parts[i]);
        }
        result[2] = rest.toString();

        return result;
    }

    // GameID Generator
    public static String generateRandomID() {
        final String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        final int length = 6;

        SecureRandom random = new SecureRandom();
        StringBuilder idBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            idBuilder.append(randomChar);
        }

        return idBuilder.toString().toUpperCase();
    }

    // File Checker
    public static boolean doesFileExist(String resourcePath) {
        InputStream inputStream = Main.class.getResourceAsStream(resourcePath);
        return inputStream != null;
    }
}