package lab08.myapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application class for the Caesar Cipher App, which provides a graphical user interface
 * for encrypting and decrypting text using the Caesar Cipher algorithm.
 */
public class CaesarCipherFXML extends Application {

    /**
     * Launch CaesarCipherFXML app.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the JavaFX application by loading the FXML layout, setting up the scene,
     * and displaying the primary stage.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(CaesarCipherFXML.class.getResource("CaesarCipherView.fxml"));
        Parent root = loader.load();

        CaesarCipherFXMLController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Caesar Cipher App");
        primaryStage.setScene(new Scene(root, 450, 350));
        primaryStage.show();
    }
}
