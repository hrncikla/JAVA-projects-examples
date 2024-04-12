package lab06;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class FibonacciPanel extends JPanel {

    private JLabel calculationStatusLabel;
    private JLabel sequenceLabel;
    private JLabel numberLabel;
    private JTextField numberField;
    private JButton calculateButton;
    private JButton cancelButton;

    JComboBox<String> sequenceComboBox;

    private FibWorker myFibWorker;
    private final String[] sequenceOptions = {"Fibonacci", "Tribonacci", "Tetranacci", "Pentanacci", "Hexanacci", "Octanacci"};

    /**
     * Constructs a new FibonacciPanel.
     */
    public FibonacciPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        initUIComponents(gbc);
        initButtonActions();
    }

    /**
     * Initializes the GUI components.
     */
    private void initUIComponents(GridBagConstraints gbc) {

        sequenceLabel = createLabel("Typ posloupnosti:");
        addComponentToPanel(sequenceLabel, gbc, 0, 0);

        sequenceComboBox = new JComboBox<>(sequenceOptions);
        addComponentToPanel(sequenceComboBox, gbc, 1, 0);

        numberLabel = createLabel("Číslo posloupnosti:");
        addComponentToPanel(numberLabel, gbc, 0, 1);

        numberField = new JTextField(15);
        addComponentToPanel(numberField, gbc, 1, 1);

        calculateButton = createButton("Vypočítat");
        addComponentToPanel(calculateButton, gbc, 0, 2);

        calculationStatusLabel = createLabel("Stav výpočtu: -");
        addComponentToPanel(calculationStatusLabel, gbc, 1, 2);

        cancelButton = createButton("Zrušit výpočet");
        addComponentToPanel(cancelButton, gbc, 0, 3);
    }

    /**
     * Adds a component to the main panel with specified grid constraints.
     */
    private void addComponentToPanel(Component component, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        add(component, gbc);
    }

    /**
     * Creates a JLabel with the specified label.
     */
    private JLabel createLabel(String label) {
        return new JLabel(label);
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
     * Initializes the actions for the buttons.
     */
    private void initButtonActions() {

        calculateButton.addActionListener(e -> {
            String input = numberField.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!input.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            final int number = Integer.parseInt(input);
            displayWait();
            myFibWorker = new FibWorker(number);
            myFibWorker.execute();
        });
        cancelButton.addActionListener(e -> myFibWorker.cancel(true));
    }

    /**
     * Adjusts the graphical interface so that it signals that a calculation is in progress.
     */
    public void displayWait() {
        calculationStatusLabel.setText("Wait!");
        calculateButton.setEnabled(false);
        cancelButton.setEnabled(true);
    }

    /**
     * It shows the result of the calculation and adds the result
     * to the result list at the same time.
     */
    public void displayResult(long value) {
        String result = "Stav výpočtu: " + value;
        calculationStatusLabel.setText(result);
        calculateButton.setEnabled(true);
        cancelButton.setEnabled(false);

        FibonacciApp fibonacciApp = (FibonacciApp) SwingUtilities.getWindowAncestor(this);
        fibonacciApp.addResultToList(result);
    }

    /**
     * It shows that the calculation was interrupted.
     */
    public void displayCancel() {
        calculationStatusLabel.setText("Cancelled");
        calculateButton.setEnabled(true);
        cancelButton.setEnabled(false);
    }


    /**
     * A class that will perform a long-term calculation on the request.
     * The result of the calculation is passed to the user interface using the function displayResult(),
     * or displayCancel() if the calculation was interrupted.
     */
    private class FibWorker extends SwingWorker<Integer, Void> {

        private final int number;

        public FibWorker(int arg) {
            super();
            this.number = arg;
        }

        @Override
        protected Integer doInBackground() throws Exception {
            String selectedSequence = (String) sequenceComboBox.getSelectedItem();
            return switch (Objects.requireNonNull(selectedSequence)) {
                case "Fibonacci" -> FibonacciVariants.fibonacci(this, number);
                case "Tribonacci" -> FibonacciVariants.tribonacci(this, number);
                case "Tetranacci" -> FibonacciVariants.tetranacci(this, number);
                case "Pentanacci" -> FibonacciVariants.pentanacci(this, number);
                case "Hexanacci" -> FibonacciVariants.hexanacci(this, number);
                case "Octanacci" -> FibonacciVariants.octanacci(this, number);
                default -> throw new Exception("The selected sequence is not supported.");
            };
        }

        @Override
        protected void done() {
            try {
                if (this.isCancelled()) displayCancel();
                else displayResult(this.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
