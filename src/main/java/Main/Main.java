package Main;

import gui.LoginGUI;
import javax.swing.*;

public static void main(String[] args) {
    try {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (Exception e) {
        e.printStackTrace();
    }

    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            new LoginGUI().setVisible(true);
        }
    });
}