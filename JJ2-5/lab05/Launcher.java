package lab05;

import javax.swing.*;

/**
 * The Launcher class is used to launch the Caesar Cipher application.
 * The application displays a GUI for encrypting and decrypting text using the Caesar cipher.
 */
public class Launcher {

    public static void main(String[] args) {
        JFrame form = new CaesarCipherApp();
        form.setVisible(true);
    }
}
