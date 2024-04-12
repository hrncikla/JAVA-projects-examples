package com.example.jj207;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * This class represents the main application window for the CaesarCipherApp.
 * It allows the user to encrypt and decrypt text using a Caesar cipher, change the shift amount, and view the results.
 */
public class CaesarCipherAppFX extends Application {

    private TextArea unencryptedTextArea;
    private TextArea encryptedTextArea;
    private Label shiftLabel;
    private Button btnEncryptButton;
    private Button btnDecryptButton;
    private Button btnChangeShiftButton;
    private int shiftAmount = 3;

    /**
     * Launch the main application window.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the primary stage of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Caesar Cipher App");
        BorderPane root = new BorderPane();

        createMenu(root);
        createUIContent(root);
        initButtonActions(primaryStage);

        Scene scene = new Scene(root, 600, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates the menu bar with menu items for encryption, decryption, and exiting the application.
     * Attaches the menu bar to the top of the specified BorderPane.
     */
    private void createMenu(BorderPane root) {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");

        MenuItem encryptMenuItem = new MenuItem("Encrypt");
        encryptMenuItem.setOnAction(e -> encrypt());

        MenuItem decryptMenuItem = new MenuItem("Decrypt");
        decryptMenuItem.setOnAction(e -> decrypt());

        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(e -> exit());

        menu.getItems().addAll(encryptMenuItem, decryptMenuItem, exitMenuItem);
        menuBar.getMenus().add(menu);
        root.setTop(menuBar);
    }

    /**
     * Creates the UI content for the application and adds it to the specified BorderPane.
     */
    private void createUIContent(BorderPane root) {

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(5);
        grid.setHgap(5);

        Label allowedCharactersLabel = new Label("Allowed characters: A-Z, space, dot");
        GridPane.setConstraints(allowedCharactersLabel, 0, 0, 4, 1);

        unencryptedTextArea = new TextArea();
        unencryptedTextArea.setWrapText(true);
        unencryptedTextArea.addEventFilter(KeyEvent.KEY_TYPED, this::filterTextAreaInput);
        GridPane.setConstraints(unencryptedTextArea, 1, 1, 2, 1);

        encryptedTextArea = new TextArea();
        encryptedTextArea.setWrapText(true);
        encryptedTextArea.addEventFilter(KeyEvent.KEY_TYPED, this::filterTextAreaInput);
        GridPane.setConstraints(encryptedTextArea, 1, 2, 2, 1);

        Label unencryptedTextLabel = new Label("Unencrypted text:");
        GridPane.setConstraints(unencryptedTextLabel, 0, 1);
        Label encryptedTextLabel = new Label("Encrypted text:");
        GridPane.setConstraints(encryptedTextLabel, 0, 2);

        btnEncryptButton = new Button("Encrypt");
        btnEncryptButton.setCursor(Cursor.HAND);
        GridPane.setConstraints(btnEncryptButton, 3, 1);
        btnDecryptButton = new Button("Decrypt");
        btnDecryptButton.setCursor(Cursor.HAND);
        GridPane.setConstraints(btnDecryptButton, 3, 2);

        shiftLabel = new Label("Shift amount: " + shiftAmount);
        GridPane.setConstraints(shiftLabel, 0, 3);

        btnChangeShiftButton = new Button("Change shift");
        btnChangeShiftButton.setCursor(Cursor.HAND);
        GridPane.setConstraints(btnChangeShiftButton, 1, 3);

        grid.getChildren().addAll(allowedCharactersLabel, unencryptedTextArea, encryptedTextArea,
                unencryptedTextLabel, encryptedTextLabel, btnEncryptButton, btnDecryptButton, shiftLabel, btnChangeShiftButton);

        root.setCenter(grid);
    }

    /**
     * Initializes the actions for the buttons.
     */
    private void initButtonActions(Stage primaryStage) {
        btnEncryptButton.setOnAction(e -> encrypt());
        btnDecryptButton.setOnAction(e -> decrypt());
        btnChangeShiftButton.setOnAction(e -> shiftAction(primaryStage));
    }

    /**
     * Filters the input in the text area to allow only uppercase letters, space, and dot.
     * If inappropriate input is detected, shows an alert.
     *
     * @param event The KeyEvent associated with the input.
     */
    private void filterTextAreaInput(KeyEvent event) {
        char inputChar = event.getCharacter().charAt(0);
        if (!(Character.isUpperCase(inputChar) || inputChar == ' ' || inputChar == '.')) {
            showAlert("Invalid Input", "Please enter only uppercase letters, space, or dot.");
            event.consume(); // Prevent input
        }
    }

    /**
     * Displays an alert with the specified title and message.
     *
     * @param title   The title of the alert.
     * @param message The message to be displayed in the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Action performed when the Change shift button is clicked - opens a dialog
     * window to change the shift amount.
     */
    private void shiftAction(Stage parentStage) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(shiftAmount));
        dialog.setTitle("Change Shift");
        dialog.setHeaderText(null);

        dialog.setContentText("Please enter the new shift amount:");
        dialog.showAndWait().ifPresent(result -> {
            try {
                shiftAmount = Integer.parseInt(result);
                shiftLabel.setText("Shift amount: " + shiftAmount);
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid input! Please enter a valid integer.");
            }
        });
    }

    /**
     * Encrypts the input text and displays the result in the encrypted text area.
     */
    public void encrypt() {
        String plaintext = unencryptedTextArea.getText();
        String encryptedText = CaesarCipher.encrypt(plaintext, shiftAmount);
        encryptedTextArea.setText(encryptedText);
    }

    /**
     * Decrypts the input text and displays the result in the unencrypted text area.
     */
    public void decrypt() {
        String encryptedText = encryptedTextArea.getText();
        String decryptedText = CaesarCipher.decrypt(encryptedText, shiftAmount);
        unencryptedTextArea.setText(decryptedText);
    }

    /**
     * Exits the application.
     */
    public void exit() {
        System.exit(0);
    }
}