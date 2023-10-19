import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class UI extends JFrame {
    // Associations
    private final Controller controller;
    private final Config config;
    // Variables
    private final JButton[][] buttons;
    private final JTextArea infoArea;
    private final JLabel triesLabel;

    // Constructor
    public UI(Config config) {
        super(config.getTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(config.isResizable());
        setLayout(new BorderLayout());

        // Associations
        this.config = config;
        this.controller = config.getController();


        // Calculate dimensions
        int buttonPanelSize = Math.min(config.getWidth(), config.getHeight());
        int buttonSize = buttonPanelSize / config.getFieldSize();
        int menuSize = Math.max(config.getWidth(), config.getHeight()) - buttonPanelSize;
        int padding = buttonPanelSize / 25;
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
        triesLabel.setBounds(padding, padding, buttonPanelSize - menuSize - 2 * padding, 2 * padding);
        triesLabel.setFont(defaultFont);
        triesLabel.setText(config.getTriesLeft() + config.getTries());
        logPanel.add(triesLabel);



        // Info Area
        infoArea = new JTextArea();
        infoArea.setBounds(padding, padding + triesLabel.getHeight(), buttonPanelSize - menuSize - 2 * padding, menuSize - 2 * padding);
        infoArea.setCaretPosition(infoArea.getText().length());
        infoArea.setBackground(Color.WHITE);
        infoArea.setFont(defaultFont);
        infoArea.setEditable(false);
        logPanel.add(infoArea);
        clearLog();

        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setBounds(padding, padding + triesLabel.getHeight(), buttonPanelSize - menuSize - 2 * padding, menuSize - 2 * padding);
        scrollPane.setVisible(true);
        logPanel.add(scrollPane);
        pack();

        // Create direction buttons
        JButton[] directionButtons = new JButton[4];
        String[] directionButtonNames = {"Up", "Left", "Down", "Right"};
        Rectangle[] directionButtonBounds = new Rectangle[4];
        ImageIcon[] directionButtonIcons = new ImageIcon[4];

        //Resize the Images
        Image[] arrows = config.getArrows();
        for (int i = 0; i < arrows.length; i++) directionButtonIcons[i] = new ImageIcon(arrows[i].getScaledInstance(menuButtonSize, menuButtonSize, Image.SCALE_DEFAULT));

        // Calculate direction button bounds
        for (int i = 0; i < 4; i++) directionButtonBounds[i] = new Rectangle();
        directionButtonBounds[0].setBounds(((menuPanel.getWidth() - menuButtonSize) / 2), menuPanel.getHeight() / 4, menuButtonSize, menuButtonSize);
        directionButtonBounds[1].setBounds(((menuPanel.getWidth() - menuButtonSize) / 2) - menuButtonSize, menuPanel.getHeight() / 4 + menuButtonSize, menuButtonSize, menuButtonSize);
        directionButtonBounds[2].setBounds(((menuPanel.getWidth() - menuButtonSize) / 2), menuPanel.getHeight() / 4 + 2 * menuButtonSize, menuButtonSize, menuButtonSize);
        directionButtonBounds[3].setBounds(((menuPanel.getWidth() - menuButtonSize) / 2) + menuButtonSize, menuPanel.getHeight() / 4 + menuButtonSize, menuButtonSize, menuButtonSize);

        // Configure direction buttons
        for (int i = 0; i < 4; i++) {
            directionButtons[i] = new JButton(directionButtonNames[i]);
            directionButtons[i].setBounds(directionButtonBounds[i]);
            directionButtons[i].setIcon(directionButtonIcons[i]);
            directionButtons[i].setToolTipText(config.getDirections(i));
            directionButtons[i].setBorder(null);
            directionButtons[i].setText("");
            int tempI = i; // Needed for lambda expression
            directionButtons[i].addActionListener(e -> controller.catPlaysMove(tempI));
            menuPanel.add(directionButtons[i]);
        }

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setPreferredSize(new Dimension(buttonPanelSize, buttonPanelSize));
        buttonPanel.setBackground(Color.RED);
        add(buttonPanel, BorderLayout.CENTER);
        pack();

        // Center JFrame
        setLocation(config.getUtils().centerFrame(this));

        // Create a button array
        buttons = new JButton[config.getFieldSize()][config.getFieldSize()];

        // Initialize Buttons
        for (int i = 0; i < config.getFieldSize(); i++) {
            for (int j = 0; j < config.getFieldSize(); j++) {
                // Create button
                buttons[i][j] = new JButton();
                buttons[i][j].setBounds(i * buttonSize, j * buttonSize, buttonSize, buttonSize);
                buttons[i][j].setBackground(Color.WHITE);
                //buttons[i][j].setBorder(null); TODO: Remove


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
        setButton(config.getData().getCat(), config.getData().getCat());
        setVisible(true); // Show UI
    }

    // Setter
    public void setButton(Point newButton, Point oldButton) {
        // For cat movement
        buttons[oldButton.x][oldButton.y].setBackground(Color.WHITE);
        buttons[newButton.x][newButton.y].setBackground(Color.BLUE);
    }

    // For obstacle placement
    public void setButton(Point newButton) {
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
        infoArea.append("\n" + message);
        infoArea.setCaretPosition(infoArea.getDocument().getLength());
    }

    // Clear log
    public void clearLog() {
        infoArea.setText(config.getCatIsOnMove());
    }
}
