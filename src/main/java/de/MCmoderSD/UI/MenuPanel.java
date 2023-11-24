package de.MCmoderSD.UI;

import de.MCmoderSD.main.Config;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.BorderLayout;

public class MenuPanel extends JPanel {

    // Associations
    private final Frame frame;
    private final Config config;

    // Constants
    private final Font defaultFont;
    private final int padding;
    private final int buttonSize;
    private final int menuButtonSize;

    // Attributes
    private JTextField roomIDField;
    private JButton hostButton;
    private JButton joinButton;
    private JButton restartButton;

    // Constructor
    public MenuPanel(Frame frame, Config config, Font defaultFont, int menuSize, int padding, int buttonSize, int menuButtonSize) {
        super();
        setLayout(null);
        setPreferredSize(new Dimension(menuSize, config.getHeight()));
        setBackground(config.getBackgroundColor());
        frame.add(this, BorderLayout.EAST);
        frame.pack();

        this.frame = frame;
        this.config = config;
        this.defaultFont = defaultFont;
        this.padding = padding;
        this.buttonSize = buttonSize;
        this.menuButtonSize = menuButtonSize;
    }

    public void initUI() {

        // Create direction buttons
        JButton[] directionButtons = new JButton[4];
        Rectangle[] directionButtonBounds = new Rectangle[4];
        ImageIcon[] directionButtonIcons = new ImageIcon[4];
        int directionButtonHeight = 2 * padding;

        //Resize the Images
        ImageIcon[] arrows = config.getArrows();
        int centeredButtonPosition = (getWidth() - buttonSize) / 2;
        for (int i = 0; i < arrows.length; i++) {
            if (config.getImageReader() != null) directionButtonIcons[i] = config.getImageReader().scaleImageIcon(arrows[i], menuButtonSize);
            else directionButtonIcons[i] = config.getImageStreamer().scaleImageIcon(arrows[i], menuButtonSize);
        }

        // Calculate direction button bounds
        for (int i = 0; i < 4; i++) directionButtonBounds[i] = new Rectangle();
        directionButtonBounds[0].setBounds(centeredButtonPosition, directionButtonHeight, menuButtonSize, menuButtonSize);
        directionButtonBounds[1].setBounds(centeredButtonPosition - menuButtonSize, directionButtonHeight + menuButtonSize, menuButtonSize, menuButtonSize);
        directionButtonBounds[2].setBounds(centeredButtonPosition, directionButtonHeight + 2 * menuButtonSize, menuButtonSize, menuButtonSize);
        directionButtonBounds[3].setBounds(centeredButtonPosition + menuButtonSize, directionButtonHeight + menuButtonSize, menuButtonSize, menuButtonSize);

        // Configure direction buttons
        for (int i = 0; i < 4; i++) {
            directionButtons[i] = new JButton();
            directionButtons[i].setBounds(directionButtonBounds[i]);
            directionButtons[i].setIcon(directionButtonIcons[i]);
            directionButtons[i].setToolTipText(config.getDirections(i));
            directionButtons[i].setBorder(null);
            int tempI = i; // Needed for lambda expression
            directionButtons[i].addActionListener(e -> frame.getController().catPlaysMove(tempI));
            add(directionButtons[i]);
        }

        // Room ID Field
        roomIDField = new JTextField();
        roomIDField.setBounds((getWidth() - 4 * menuButtonSize) / 2 - padding, getHeight() - 3 * menuButtonSize, 4 * menuButtonSize, menuButtonSize);
        roomIDField.setFont(defaultFont);
        if (config.getGameID() != null) hideMultiplayerComponents();
        add(roomIDField);

        // Host Button
        hostButton = new JButton();
        hostButton.setBounds((getWidth() - 4 * menuButtonSize) / 2 - padding, getHeight() - menuButtonSize, 4 * menuButtonSize, menuButtonSize);
        hostButton.setText(config.getHost());
        hostButton.setFont(defaultFont);
        hostButton.addActionListener(e -> frame.getController().hostGame());
        if (config.getGameID() != null) hideMultiplayerComponents();
        add(hostButton);

        // Join Button
        joinButton = new JButton();
        joinButton.setBounds((getWidth() - 4 * menuButtonSize) / 2 - padding, getHeight() - 2 * menuButtonSize, 4 * menuButtonSize, menuButtonSize);
        joinButton.setText(config.getJoin());
        joinButton.setFont(defaultFont);
        joinButton.addActionListener(e -> frame.getController().joinGame());
        if (config.getGameID() != null) hideMultiplayerComponents();
        add(joinButton);

        // Restart Button
        restartButton = new JButton();
        restartButton.setBounds(joinButton.getBounds());
        restartButton.setText(config.getRestart());
        restartButton.setFont(defaultFont);
        restartButton.addActionListener(e -> frame.getController().restartGame());
        restartButton.setVisible(false);
        add(restartButton);
    }

    // Hide multiplayer components
    public void hideMultiplayerComponents() {
        if (roomIDField != null) roomIDField.setVisible(false);
        if (hostButton != null) hostButton.setVisible(false);
        if (joinButton != null) joinButton.setVisible(false);
    }

    // Returns the room ID from the room ID field
    public String getRoomID() {
        return roomIDField.getText().toUpperCase();
    }

    // Show restart button
    public void setRestartButtonVisible(boolean visible) {
        if (config.getGameID() != null) hideMultiplayerComponents();
        restartButton.setVisible(visible);
    }
}
