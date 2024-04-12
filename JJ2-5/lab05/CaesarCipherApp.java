package lab05;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class represents the main application window for the CaesarCipherApp.
 * It allows the user to encrypt and decrypt text using a Caesar cipher, change the shift amount, and view the results.
 */
public class CaesarCipherApp extends JFrame {

    private final JPanel mainPanel;
    private JLabel unencryptedTextLabel;
    private JLabel encryptedTextLabel;
    private JLabel shiftLabel;
    private JTextArea unencryptedTextArea;
    private JTextArea encryptedTextArea;
    private JButton btnEncryptButton;
    private JButton btnDecryptButton;
    private JButton btnChangeShiftButton;
    private int shiftAmount = 3;

    private final Color activeColor = new Color(173, 216, 230);
    private final Color defaultColor = UIManager.getColor("TextArea.background");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CaesarCipherApp().setVisible(true);
            }
        });
    }

    /**
     * Initializes the main application window.
     */
    public CaesarCipherApp() {
        setTitle("Caesar Cipher App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        initUIComponents(gbc);
        initButtonActions();

        setContentPane(mainPanel);
        setPreferredSize(new Dimension(600, 300));
        pack();
    }

    /**
     * Initializes the UI components.
     */
    private void initUIComponents(GridBagConstraints gbc) {
        CaesarCipherAppMenu menuBar = new CaesarCipherAppMenu(this);
        setJMenuBar(menuBar);

        unencryptedTextLabel = createLabel("Unencrypted text:");
        addComponentToPanel(unencryptedTextLabel, gbc, 0, 0);

        unencryptedTextArea = createTextArea(2, 30);
        gbc.gridwidth = 2;
        addComponentToPanel(new JScrollPane(unencryptedTextArea), gbc, 1, 0);

        btnEncryptButton = createButton("Encrypt");
        addComponentToPanel(btnEncryptButton, gbc, 3, 0);

        encryptedTextLabel = createLabel("Encrypted text:");
        addComponentToPanel(encryptedTextLabel, gbc, 0, 1);

        encryptedTextArea = createTextArea(2, 30);
        addComponentToPanel(new JScrollPane(encryptedTextArea), gbc, 1, 1);

        btnDecryptButton = createButton("Decrypt");
        addComponentToPanel(btnDecryptButton, gbc, 3, 1);

        shiftLabel = createLabel("Shift amount: " + shiftAmount);
        addComponentToPanel(shiftLabel, gbc, 0, 2);

        btnChangeShiftButton = createButton("Change shift");
        gbc.gridwidth = 3;
        addComponentToPanel(btnChangeShiftButton, gbc, 1, 2);

        unencryptedTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTextFieldBackground(unencryptedTextArea, true);
                setTextFieldBackground(encryptedTextArea, false);
            }
        });

        encryptedTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTextFieldBackground(unencryptedTextArea, false);
                setTextFieldBackground(encryptedTextArea, true);
            }
        });
    }

    /**
     * Sets the background color of the text area based on its active state.
     */
    private void setTextFieldBackground(JTextArea textField, boolean isActive) {
        Color background = isActive ? activeColor : defaultColor;
        textField.setBackground(background);
    }

    /**
     * Initializes the actions for the buttons.
     */
    private void initButtonActions() {
        btnDecryptButton.addActionListener(e -> decrypt());
        btnEncryptButton.addActionListener(e -> encrypt());
        btnChangeShiftButton.addActionListener(this::shiftAction);
    }

    /**
     * Adds a component to the main panel with specified grid constraints.
     */
    private void addComponentToPanel(Component component, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        mainPanel.add(component, gbc);
    }

    /**
     * Creates a JLabel with the specified label.
     */
    private JLabel createLabel(String label) {
        return new JLabel(label);
    }

    /**
     * Creates a JTextArea with the specified number of rows and columns.
     */
    private JTextArea createTextArea(int rows, int columns) {
        JTextArea textArea = new JTextArea(rows, columns);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return textArea;
    }

    /**
     * Creates a JButton with the specified label.
     */
    private JButton createButton(String label) {
        JButton newButton = new JButton(label);
        newButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return newButton;
    }

    /**
     * Action performed when the Change shift button is clicked - opens a dialog
     * window to change the shift amount.
     */
    private void shiftAction(ActionEvent e) {
        ShiftDialog dlg = new ShiftDialog(this);
        dlg.setModal(true);
        dlg.setVisible(true);

        if (dlg.getResult() != null) {
            shiftAmount = dlg.getResult();
            shiftLabel.setText("Shift Amount: " + shiftAmount);
        }
        dlg.dispose();
    }

    /**
     * Validates if the input text contains only valid characters.
     */
    private boolean isValidInput(String input) {
        for (char character : input.toCharArray()) {
            if (!(character >= 'A' && character <= 'Z' || character == ' ' || character == '.')) {
                return false;
            }
        }
        return true;
    }

    /**
     * Encrypts the input text and displays the result in the encrypted text area.
     */
    public void encrypt() {
        String plaintext = unencryptedTextArea.getText();
        if (isValidInput(plaintext)) {
            String encryptedText = CaesarCipher.encrypt(plaintext, shiftAmount);
            encryptedTextArea.setText(encryptedText);
        } else {
            JOptionPane.showMessageDialog(CaesarCipherApp.this, "Invalid input. Only A-Z, space, and dot are allowed.");
        }
    }

    /**
     * Decrypts the input text and displays the result in the unencrypted text area.
     */
    public void decrypt() {
        String encryptedText = encryptedTextArea.getText();
        if (isValidInput(encryptedText)) {
            String decryptedText = CaesarCipher.decrypt(encryptedText, shiftAmount);
            unencryptedTextArea.setText(decryptedText);
        } else {
            JOptionPane.showMessageDialog(CaesarCipherApp.this, "Invalid input. Only A-Z, space, and dot are allowed.");
        }
    }
}
