package de.MCmoderSD.UI;

import de.MCmoderSD.core.Controller;
import de.MCmoderSD.main.Config;
import de.MCmoderSD.utilities.Calculate;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.Point;
import java.awt.BorderLayout;
import java.awt.Font;

@SuppressWarnings("unused")
public class Frame extends JFrame {

    // Associations
    private final Controller controller;

    // Attributes
    private final MenuPanel menuPanel;
    private final InfoPanel infoPanel;
    private final ButtonPanel buttonPanel;

    // Constructor
    public Frame(Config config) {
        super(config.getTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(config.isResizable());
        setLayout(new BorderLayout());

        // Calculate dimensions
        int max = Math.max(config.getWidth(), config.getHeight());
        int min = Math.min(config.getWidth(), config.getHeight());
        int padding = max / 50;
        int buttonPanelSize = min + padding * 2;
        int buttonSize = min / config.getFieldSize();
        int menuSize = max - buttonPanelSize - 2 * padding;
        int menuButtonSize = 2 * padding;
        Font defaultFont = new Font("Roboto", Font.PLAIN, 20);

        // Create MenuPanel Panel
        menuPanel = new MenuPanel(this, config, defaultFont, menuSize, padding, buttonSize, menuButtonSize);

        // Create Info Panel
        infoPanel = new InfoPanel(this, config, defaultFont, buttonPanelSize, menuSize, padding);

        // Create Button Panel
        buttonPanel = new ButtonPanel(this, config, buttonPanelSize, padding, buttonSize);

        // Initialize UI
        menuPanel.initUI();
        infoPanel.initUI();
        buttonPanel.initUI();
        pack();
        repaint();

        // Inputs
        InputHandler inputHandler = new InputHandler(this);
        //new Timer(100, e -> requestFocusInWindow()).start();

        setLocation(Calculate.centerFrame(this));
        controller = new Controller(this, config);
    }

    // Getter
    public Controller getController() {
        return controller;
    }

    public String getRoomID() {
        return menuPanel.getRoomID();
    }

    // Setter
    public void hideMultiplayerComponents() {
        menuPanel.hideMultiplayerComponents();
    }

    public void clearLog() {
        infoPanel.clearLog();
    }

    public void appendLog(String message) {
        infoPanel.appendLog(message);
    }

    public void updateTries(int tries) {
        infoPanel.updateTries(tries);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void setObstacle(Point newButton) {
        buttonPanel.setObstacle(newButton);
    }

    public void setCat(Point newButton) {
        buttonPanel.setCat(newButton);
    }
}
