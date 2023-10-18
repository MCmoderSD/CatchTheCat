import com.sun.xml.internal.ws.util.xml.CDATA;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UI extends JFrame {
    // Associations
    private final Config config;
    private final Data data;
    private final Controller controller;

    // Variables
    private final JButton[][] buttons;
    private final ArrayList<JButton> buttonList = new ArrayList<>();

    // Constructor
    public UI(Config config) {
        super(config.getTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(config.getDimension());
        setResizable(config.isResizable());
        setLayout(new BorderLayout());

        this.config = config;
        this.controller = config.getController();
        this.data = config.getData();

        int buttonPanelSize = Math.min(config.getWidth(), config.getHeight());
        int buttonSize = buttonPanelSize / config.getFieldSize();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setPreferredSize(new Dimension(buttonPanelSize, buttonPanelSize));
        buttonPanel.setBackground(Color.RED);
        add(buttonPanel, BorderLayout.CENTER);

        buttons = new JButton[config.getFieldSize()][config.getFieldSize()];

        for (int i = 0; i < config.getFieldSize(); i++) {
            for (int j = 0; j < config.getFieldSize(); j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setBounds(i * buttonSize, j * buttonSize, buttonSize, buttonSize);
                int tempI = i;
                int tempJ = j;
                buttons[i][j].addActionListener(e -> buttonPressed(new Point(tempI, tempJ)));
                buttonPanel.add(buttons[i][j]);
                buttonList.add(buttons[i][j]);
            }
        }

        setVisible(true);
    }

    private void buttonPressed(Point point) {
        System.out.println("Button pressed: " + point);
    }

    // Setter
    public void setButton(Point point, boolean isCat) {
        if (isCat) buttons[point.x][point.y].setBackground(Color.BLUE);
        else buttons[point.x][point.y].setBackground(Color.GREEN);
    }
}
