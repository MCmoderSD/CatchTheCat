package de.MCmoderSD.UI;

import de.MCmoderSD.main.Config;
import de.MCmoderSD.utilities.image.ImageReader;
import de.MCmoderSD.utilities.image.ImageStreamer;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.BorderLayout;

public class ButtonPanel extends JPanel {

    // Associations
    private final Frame frame;
    private final Config config;
    private final ImageReader imageReader;
    private final ImageStreamer imageStreamer;

    // Constants
    private final int padding;
    private final int buttonSize;

    // Attributes
    private JButton[][] buttons;

    // Constructor
    public ButtonPanel(Frame frame, Config config, int buttonPanelSize, int padding, int buttonSize) {
        super();
        setLayout(null);
        setPreferredSize(new Dimension(buttonPanelSize, buttonPanelSize));
        setBackground(config.getBackgroundColor());
        frame.add(this, BorderLayout.WEST);
        frame.pack();

        imageReader = config.getImageReader();
        imageStreamer = config.getImageStreamer();

        this.frame = frame;
        this.config = config;
        this.padding = padding;
        this.buttonSize = buttonSize;
    }

    public void initUI() {
        // Create a 2D array of buttons
        buttons = new JButton[config.getFieldSize()][config.getFieldSize()];

        // Initialize Buttons
        for (int i = 0; i < config.getFieldSize(); i++) {
            for (int j = 0; j < config.getFieldSize(); j++) {
                // Create button
                buttons[i][j] = new JButton();
                buttons[i][j].setBounds(padding + i * buttonSize, padding + j * buttonSize, buttonSize, buttonSize);
                buttons[i][j].setBackground(config.getBackgroundColor());

                // Action listener
                int tempI = i; // Needed for lambda expression
                int tempJ = j; // Needed for lambda expression
                buttons[i][j].addActionListener(e -> frame.getController().placeObstacle(new Point(tempI, tempJ)));
                add(buttons[i][j]); // Add button to panel
            }
        }
    }

    // Draw the grid
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics;

        g.setColor(config.getFontColor());
        g.drawRect(padding - 1, padding - 1, config.getFieldSize() * buttonSize + 1, config.getFieldSize() * buttonSize + 1);
    }

    // For obstacle placement
    public void setObstacle(Point obstaclePosition) {
        JButton button = buttons[obstaclePosition.x][obstaclePosition.y];
        button.setBackground(config.getBackgroundColor());
        button.setIcon(imageReader != null ? imageReader.scaleImageIcon(config.getObstacleImage(), buttonSize) : imageStreamer.scaleImageIcon(config.getObstacleImage(), buttonSize));
    }

    // For cat placement
    public void setCat(Point catPosition) {

        for (int i = 0; i < config.getFieldSize(); i++) for (int j = 0; j < config.getFieldSize(); j++) {
            buttons[i][j].setBackground(config.getBackgroundColor());
            buttons[i][j].setIcon(null);
        }
        
        JButton button = buttons[catPosition.x][catPosition.y];
        button.setBackground(config.getCatColor());
        button.setIcon(imageReader != null ? imageReader.scaleImageIcon(config.getCatImage(), buttonSize) : imageStreamer.scaleImageIcon(config.getCatImage(), buttonSize));
    }
}
