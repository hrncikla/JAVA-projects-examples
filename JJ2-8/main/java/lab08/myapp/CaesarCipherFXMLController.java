package lab08.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Controller class for the Caesar Cipher application's FXML view.
 * Handles user interactions and logic for encrypting and decrypting text.
 */
public class CaesarCipherFXMLController {

    private Stage primaryStage;
    private int shiftAmount = 3;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Sets the primary stage for the controller.
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private TextArea unencryptedTextArea;

    @FXML
    private TextArea encryptedTextArea;

    @FXML
    private Label shiftLabel;

    @FXML
    private void initialize() {
        shiftLabel.setText("Shift amount: " + shiftAmount);
    }

    /**
     * Filters the input in the text area to allow only uppercase letters, space, and dot.
     * If inappropriate input is detected, shows an alert.
     *
     * @param event The KeyEvent associated with the input.
     */
    @FXML
    private void filterTextAreaInput(KeyEvent event) {
        char inputChar = event.getCharacter().charAt(0);
        if (!(Character.isUpperCase(inputChar) || inputChar == ' ' || inputChar == '.')) {
            showAlert("Invalid Input", "Please enter only uppercase letters, space, or dot.");
            unencryptedTextArea.clear();
            encryptedTextArea.clear();
            event.consume();
        }
    }

    /**
     * Action performed when the Change shift button is clicked - opens a dialog
     * window to change the shift amount.
     */
    @FXML
    private void shiftAction(ActionEvent event) {
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
    @FXML
    private void encrypt() {
        String plaintext = unencryptedTextArea.getText();
        String encryptedText = CaesarCipher.encrypt(plaintext, shiftAmount);
        encryptedTextArea.setText(encryptedText);
    }

    /**
     * Decrypts the input text and displays the result in the unencrypted text area.
     */
    @FXML
    private void decrypt() {
        String encryptedText = encryptedTextArea.getText();
        String decryptedText = CaesarCipher.decrypt(encryptedText, shiftAmount);
        unencryptedTextArea.setText(decryptedText);
    }

    /**
     * Exits the application.
     */
    @FXML
    public void exit() {
        primaryStage.close();
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
}
