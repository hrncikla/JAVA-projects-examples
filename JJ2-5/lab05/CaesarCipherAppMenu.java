package lab05;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents the menu bar for the CaesarCipherApp.
 * It allows the user to perform actions such as encryption, decryption, and exiting the application.
 */
public class CaesarCipherAppMenu extends JMenuBar {

    private final CaesarCipherApp parent;

    public CaesarCipherAppMenu(CaesarCipherApp parent) {
        this.parent = parent;
        initializeMenu();
    }

    private void initializeMenu() {
        JMenu appMenu = new JMenu("Menu");
        appMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JMenuItem encryptMenuItem = createMenuItem("Encrypt");
        JMenuItem decryptMenuItem = createMenuItem("Decrypt");
        JMenuItem exitMenuItem = createMenuItem("Exit");

        appMenu.add(encryptMenuItem);
        appMenu.add(decryptMenuItem);
        appMenu.addSeparator();
        appMenu.add(exitMenuItem);

        encryptMenuItem.addActionListener(e -> parent.encrypt());
        decryptMenuItem.addActionListener(e -> parent.decrypt());
        exitMenuItem.addActionListener(e -> System.exit(0));

        add(appMenu);
    }

    private JMenuItem createMenuItem(String label) {
        JMenuItem newButton = new JMenuItem(label);
        newButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return newButton;
    }
}