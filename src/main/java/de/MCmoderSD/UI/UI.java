package de.MCmoderSD.UI;

import de.MCmoderSD.core.Controller;
import de.MCmoderSD.main.Config;
import de.MCmoderSD.utilities.Calculate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class UI extends JFrame {

    // Associations
    private final Controller controller;
    private final Config config;

    // Attributes
    private final JButton[][] buttons;
    private final JButton hostButton, joinButton;
    private final JTextField roomIDField;
    private final JTextArea infoArea;
    private final JLabel triesLabel;

    // Variables
    private String tempLog;

    // Constructor
    public UI(Config config) {
        super(config.getTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(config.isResizable());
        setLayout(new BorderLayout());

        // Associations
        this.config = config;
        this.controller = config.getController();
        config.setUI(this);

        // Calculate dimensions
        int max = Math.max(config.getWidth(), config.getHeight());
        int min = Math.min(config.getWidth(), config.getHeight());
        int padding = max / 50;
        int buttonPanelSize = min + padding * 2;
        int buttonSize = min / config.getFieldSize();
        int menuSize = max - buttonPanelSize - 2 * padding;
        int menuButtonSize = 2 * padding;
        Font defaultFont = new Font("Roboto", Font.PLAIN, 20);


        // Menu Bar
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(null);
        menuPanel.setPreferredSize(new Dimension(menuSize, config.getHeight()));
        menuPanel.setBackground(Color.WHITE);
        add(menuPanel, BorderLayout.EAST);

        // Log Bar
        JPanel logPanel = new JPanel();
        logPanel.setLayout(null);
        logPanel.setPreferredSize(new Dimension(buttonPanelSize, menuSize));
        logPanel.setBackground(Color.WHITE);
        add(logPanel, BorderLayout.SOUTH);

        // Tries Label
        triesLabel = new JLabel();
        triesLabel.setBounds(padding, 0, buttonPanelSize, padding * 2);
        triesLabel.setText(config.getTriesLeft() + config.getTries());
        triesLabel.setFont(defaultFont);
        logPanel.add(triesLabel);

        // Info Area
        infoArea = new JTextArea();
        infoArea.setBounds(padding, triesLabel.getHeight(), buttonPanelSize - 2 * padding, menuSize - 2 * padding - triesLabel.getHeight());
        infoArea.setCaretPosition(infoArea.getText().length());
        infoArea.setBackground(Color.WHITE);
        infoArea.setFont(defaultFont);
        infoArea.setEditable(false);
        logPanel.add(infoArea);
        clearLog();

        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setBounds(padding, triesLabel.getHeight(), buttonPanelSize - 2 * padding, menuSize - triesLabel.getHeight());
        scrollPane.setBorder(null);
        logPanel.add(scrollPane);
        pack();

        // Create direction buttons
        JButton[] directionButtons = new JButton[4];
        Rectangle[] directionButtonBounds = new Rectangle[4];
        ImageIcon[] directionButtonIcons = new ImageIcon[4];
        int directionButtonHeight = 2 * padding;

        //Resize the Images
        BufferedImage[] arrows = config.getArrows();
        int centeredButtonPosition = (menuPanel.getWidth() - buttonSize) / 2;
        for (int i = 0; i < arrows.length; i++) {
            if (config.getImageReader() != null) directionButtonIcons[i] = config.getImageReader().createImageIcon(arrows[i], buttonSize);
            else directionButtonIcons[i] = config.getImageStreamer().createImageIcon(arrows[i], buttonSize);
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
            directionButtons[i].addActionListener(e -> controller.catPlaysMove(tempI));
            menuPanel.add(directionButtons[i]);
        }

        // Room ID Field
        roomIDField = new JTextField();
        roomIDField.setBounds((menuPanel.getWidth() - 3 * menuButtonSize) / 2 - padding, menuPanel.getHeight() - 3 * menuButtonSize, 3 * menuButtonSize, menuButtonSize);
        roomIDField.setFont(defaultFont);
        if (config.getGameID() != null) hideMultiplayerComponents();
        menuPanel.add(roomIDField);

        // Host Button
        hostButton = new JButton();
        hostButton.setBounds((menuPanel.getWidth() - 3 * menuButtonSize) / 2 - padding, menuPanel.getHeight() - menuButtonSize, 3 * menuButtonSize, menuButtonSize);
        hostButton.setText(config.getHost());
        hostButton.setFont(defaultFont);
        hostButton.addActionListener(e -> controller.hostGame());
        if (config.getGameID() != null) hideMultiplayerComponents();
        menuPanel.add(hostButton);

        // Join Button
        joinButton = new JButton();
        joinButton.setBounds((menuPanel.getWidth() - 3 * menuButtonSize) / 2 - padding, menuPanel.getHeight() - 2 * menuButtonSize, 3 * menuButtonSize, menuButtonSize);
        joinButton.setText(config.getJoin());
        joinButton.setFont(defaultFont);
        joinButton.addActionListener(e -> controller.joinGame());
        if (config.getGameID() != null) hideMultiplayerComponents();
        menuPanel.add(joinButton);

        // Create button panel
        JPanel buttonPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.drawRect(padding - 1, padding - 1, config.getFieldSize() * buttonSize + 1, config.getFieldSize() * buttonSize + 1);
            }
        };

        buttonPanel.setLayout(null);
        buttonPanel.setPreferredSize(new Dimension(buttonPanelSize, buttonPanelSize));
        buttonPanel.setBackground(Color.WHITE);
        add(buttonPanel, BorderLayout.WEST);
        pack();

        // Center JFrame
        setLocation(Calculate.centerFrame(this));

        // Create a button array
        buttons = new JButton[config.getFieldSize()][config.getFieldSize()];

        // Initialize Buttons
        for (int i = 0; i < config.getFieldSize(); i++) {
            for (int j = 0; j < config.getFieldSize(); j++) {
                // Create button
                buttons[i][j] = new JButton();
                buttons[i][j].setBounds(padding + i * buttonSize, padding + j * buttonSize, buttonSize, buttonSize);
                buttons[i][j].setBackground(Color.WHITE);

                // Action listener
                int tempI = i; // Needed for lambda expression
                int tempJ = j; // Needed for lambda expression
                buttons[i][j].addActionListener(e -> controller.placeObstacle(new Point(tempI, tempJ)));
                buttonPanel.add(buttons[i][j]); // Add button to panel
            }
        }

        // Key listener
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode(); // Get key code
                boolean isWASD = key == KeyEvent.VK_W || key == KeyEvent.VK_A || key == KeyEvent.VK_S || key == KeyEvent.VK_D; // Check if key is WASD
                boolean isArrow = key == KeyEvent.VK_UP || key == KeyEvent.VK_LEFT || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_RIGHT; // Check if the key is Arrow

                // Check if the key is WASD or Arrow
                if (isWASD || isArrow) controller.catPlaysMove(key);
            }
        });

        // Set cat's position
        setCat(config.getData().getCat());
        setVisible(true); // Show UI
    }

    // Setter

    // For cat movement
    public void setCat(Point newButton) {
        for (int i = 0; i < config.getFieldSize(); i++) for (int j = 0; j < config.getFieldSize(); j++) buttons[i][j].setBackground(Color.WHITE);
        buttons[newButton.x][newButton.y].setBackground(Color.BLUE);
    }

    // For obstacle placement
    public void setObstacle(Point newButton) {
        buttons[newButton.x][newButton.y].setBackground(Color.RED);
    }

    // Show message dialog
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    // Update tries
    public void updateTries(int tries) {
        triesLabel.setText(config.getTriesLeft() + tries);
    }

    // Append to log
    public void appendLog(String message) {
        if (Objects.equals(message, tempLog)) return;
        else tempLog = message;
        infoArea.append("\n" + message);
        infoArea.setCaretPosition(infoArea.getDocument().getLength());
    }

    // Clear log
    public void clearLog() {
        infoArea.setText(config.getCatIsOnMove());
    }

    // Hide Host Button
    public void hideMultiplayerComponents() {
        if (roomIDField != null) roomIDField.setVisible(false);
        if (hostButton != null) hostButton.setVisible(false);
        if (joinButton != null) joinButton.setVisible(false);
    }

    public void hideOldFrame() {
        setVisible(false);
    }

    // Getter
    public String getRoomID() {
        return roomIDField.getText();
    }
}
