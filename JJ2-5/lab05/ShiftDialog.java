package lab05;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents a dialog window for changing the shift amount.
 * It allows the user to input a new shift value and provides OK and Cancel buttons to confirm or cancel the operation.
 */
public class ShiftDialog extends JDialog {

    private final JLabel shiftLabel;
    private final JTextField shiftTextField;
    private final JButton okButton;
    private final JButton cancelButton;
    private Integer result;

    public ShiftDialog(JFrame parent) {
        super(parent, "Change Shift", true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        shiftLabel = new JLabel("Shift Amount:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(shiftLabel, gbc);

        shiftTextField = new JTextField(10);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(shiftTextField, gbc);

        okButton = new JButton("OK");
        okButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(okButton, gbc);

        cancelButton = new JButton("Cancel");
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(cancelButton, gbc);

        okButton.addActionListener(e -> {
            try {
                result = Integer.parseInt(shiftTextField.getText());
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ShiftDialog.this, "Invalid input! Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> {
            result = null;
            dispose();
        });

        add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    public Integer getResult() {
        return result;
    }
}
