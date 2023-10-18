import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class UI extends JFrame {
    // Associations
    private final Data data;
    private final Controller controller;

    // Variables
    private final JButton[][] buttons;

    // Constructor
    public UI(Config config) {
        super(config.getTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(config.isResizable());
        setLayout(new BorderLayout());

        // Associations
        this.controller = config.getController();
        this.data = config.getData();

        // Calculate dimensions
        int buttonPanelSize = Math.min(config.getWidth(), config.getHeight());
        int buttonSize = buttonPanelSize / config.getFieldSize();

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

                // Action listener
                int tempI = i; // Needed for lambda expression
                int tempJ = j; // Needed for lambda expression
                buttons[i][j].addActionListener(e -> {
                    Point point = new Point(tempI, tempJ);
                    if (data.isCatOnMove()) controller.catPlaysMove(point);
                    else controller.placeObstacle(point);
                });
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

                // Check if the key is WASD or Arrow and if the cat is on the move
                if (data.isCatOnMove() && isWASD || isArrow) controller.catPlaysMove(key);
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
}
