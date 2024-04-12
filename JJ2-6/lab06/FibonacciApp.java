package lab06;

import javax.swing.*;
import java.awt.*;

/**
 * FibonacciApp class represents an application for generating Fibonacci sequences.
 * It provides a graphical user interface (GUI) to calculate and display Fibonacci sequences.
 */
public class FibonacciApp extends JFrame {

    private final DefaultListModel<String> resultListModel;
    private final JList<String> resultList;

    private final JPanel mainPanel;
    private FibonacciPanel sequencePanel1;
    private FibonacciPanel sequencePanel2;
    private FibonacciPanel sequencePanel3;
    private JScrollPane resultScrollPanel;

    /**
     * Launches the FibonacciApp application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FibonacciApp().setVisible(true);
            }
        });
    }

    /**
     * Constructs a new FibonacciApp object.
     * Sets up the main frame.
     */
    public FibonacciApp() {
        setTitle("Fibonacci App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        resultListModel = new DefaultListModel<>();
        resultList = new JList<>(resultListModel);

        initUIComponents(gbc);

        setContentPane(mainPanel);
        setPreferredSize(new Dimension(1400, 400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Initializes the GUI components.
     * Adds FibonacciPanel instances to the main panel.
     */
    private void initUIComponents(GridBagConstraints gbc) {

        sequencePanel1 = new FibonacciPanel();
        addComponentToPanel(sequencePanel1, gbc, 0, 0);

        sequencePanel2 = new FibonacciPanel();
        addComponentToPanel(sequencePanel2, gbc, 1, 0);

        sequencePanel3 = new FibonacciPanel();
        addComponentToPanel(sequencePanel3, gbc, 2, 0);

        resultScrollPanel = new JScrollPane(resultList);
        resultScrollPanel.setPreferredSize(new Dimension(200, resultScrollPanel.getPreferredSize().height));
        addComponentToPanel(resultScrollPanel, gbc, 3, 0);
    }

    /**
     * Adds a component to the main panel.
     */
    private void addComponentToPanel(Component component, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        mainPanel.add(component, gbc);
    }

    /**
     * Adds a result to the list displayed in the GUI.
     */
    public void addResultToList(String result) {
        int resultCount = resultListModel.size() + 1;
        String numberedResult = resultCount + ". " + result;
        resultListModel.addElement(numberedResult);
    }
}