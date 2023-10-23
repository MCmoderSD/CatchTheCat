package de.MCmoderSD.utilities;

import de.MCmoderSD.data.Data;
import de.MCmoderSD.main.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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

    public static String encodeData(Data data, Config config) {
        String end = ";";

        // Encode Config Data
        String confPack = config.getFieldSize() + end + config.getTries();

        // Encode Data
        int triesLeft = config.getTries();
        String dataPack;
        String[] obstaclePackTemp = new String[config.getTries()];
        Point[] obstacles = data.getObstacles();
        for (int i = 0; i < config.getTries(); i++) {
            if (obstacles[i] != null) {
                obstaclePackTemp[i] = String.valueOf(obstacles[i].x) + ':' + obstacles[i].y;
                --triesLeft;
            }
        }

        String[] obstaclePack = new String[config.getTries()-triesLeft];
        for (int i = 0; i < obstaclePack.length; i++) {
            if (obstaclePackTemp[i] != null) obstaclePack[i] = obstaclePackTemp[i];
        }

        dataPack = triesLeft + end + data.getCat().x + ':' + data.getCat().y + end + String.join(end, obstaclePack);

        // Finalize Package
        return confPack + end + dataPack;
    }

    // Decoder
    private static void decodeData(String dataPack) {

    }
}