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

        this.controller = config.getController();
        this.data = config.getData();

        int buttonPanelSize = Math.min(config.getWidth(), config.getHeight());
        int buttonSize = buttonPanelSize / config.getFieldSize();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setPreferredSize(new Dimension(buttonPanelSize, buttonPanelSize));
        buttonPanel.setBackground(Color.RED);
        add(buttonPanel, BorderLayout.CENTER);
        pack();

        buttons = new JButton[config.getFieldSize()][config.getFieldSize()];

        for (int i = 0; i < config.getFieldSize(); i++) {
            for (int j = 0; j < config.getFieldSize(); j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setBounds(i * buttonSize, j * buttonSize, buttonSize, buttonSize);
                buttons[i][j].setBackground(Color.WHITE);
                int tempI = i;
                int tempJ = j;
                buttons[i][j].addActionListener(e -> {
                    Point point = new Point(tempI, tempJ);
                    if (data.isCatOnMove()) controller.catPlaysMove(point);
                    else controller.placeObstacle(point);
                });
                buttonPanel.add(buttons[i][j]);
            }
        }


        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode();
                boolean isWASD = key == KeyEvent.VK_W || key == KeyEvent.VK_A || key == KeyEvent.VK_S || key == KeyEvent.VK_D;
                boolean isArrow = key == KeyEvent.VK_UP || key == KeyEvent.VK_LEFT || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_RIGHT;
                if (data.isCatOnMove() && isWASD || isArrow) {
                    controller.catPlaysMove(key);
                    System.out.println(key);
                }
            }
        });

        setButton(config.getData().getCatPosition(), config.getData().getCatPosition());
        setVisible(true);
    }

    // Setter
    public void setButton(Point newButton, Point oldButton) {
        buttons[oldButton.x][oldButton.y].setBackground(Color.WHITE);
        buttons[newButton.x][newButton.y].setBackground(Color.BLUE);
    }

    public void setButton(Point newButton) {
        buttons[newButton.x][newButton.y].setBackground(Color.RED);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
