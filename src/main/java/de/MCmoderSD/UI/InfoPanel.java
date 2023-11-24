package de.MCmoderSD.UI;

import de.MCmoderSD.main.Config;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.util.Objects;

public class InfoPanel extends JPanel {

    // Associations
    private final Config config;

    // Constants
    private final Font defaultFont;
    private final int padding;
    private final int buttonPanelSize;
    private final int menuSize;

    // Attributes
    private JLabel triesLabel;
    private JTextArea infoArea;
    private String tempLog;

    // Constructor
    public InfoPanel(Frame frame, Config config, Font defaultFont, int buttonPanelSize, int menuSize, int padding) {
        super();
        setLayout(null);
        setPreferredSize(new Dimension(buttonPanelSize, menuSize));
        setBackground(config.getBackgroundColor());
        frame.add(this, BorderLayout.SOUTH);
        frame.pack();

        this.config = config;
        this.defaultFont = defaultFont;
        this.padding = padding;
        this.buttonPanelSize = buttonPanelSize;
        this.menuSize = menuSize;
    }

    public void initUI() {

        // Tries Label
        triesLabel = new JLabel();
        triesLabel.setBounds(padding, 0, buttonPanelSize, padding * 2);
        triesLabel.setText(config.getTriesLeft() + config.getTries());
        triesLabel.setFont(defaultFont);
        add(triesLabel);

        // Info Area
        infoArea = new JTextArea();
        infoArea.setBounds(padding, triesLabel.getHeight(), buttonPanelSize - 2 * padding, menuSize - 2 * padding - triesLabel.getHeight());
        infoArea.setCaretPosition(infoArea.getText().length());
        infoArea.setBackground(config.getBackgroundColor());
        infoArea.setFont(defaultFont);
        infoArea.setEditable(false);
        add(infoArea);
        clearLog();

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setBounds(padding, triesLabel.getHeight(), buttonPanelSize - 2 * padding, menuSize - triesLabel.getHeight());
        scrollPane.setBorder(null);
        add(scrollPane);
    }

    // Clear log
    public void clearLog() {
        infoArea.setText("");
    }

    // Append log
    public void appendLog(String message) {
        if (Objects.equals(message, tempLog)) return;
        else tempLog = message;
        infoArea.append("\n" + message);
        infoArea.setCaretPosition(infoArea.getDocument().getLength());
    }

    // Update tries
    public void updateTries(int tries) {
        triesLabel.setText(config.getTriesLeft() + tries);
    }
}
