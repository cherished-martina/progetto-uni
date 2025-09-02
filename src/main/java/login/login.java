package login;

import controller.UtenteController;
import gui.MainGUI;
import gui.RegistrazioneGUI;
import model.Utente;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class login extends JFrame {
    // sotto mettiamo le componenti dell'interfaccia
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registratiButton;
    private UtenteController utenteController;


    public login() {
        this.utenteController = new UtenteController();
    }


    private void eseguiLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci username e password!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Utente utente = utenteController.login(username, password);

        if (utente != null) {
            JOptionPane.showMessageDialog(this,
                    "Benvenuto, " + utente.getNome() + " " + utente.getCognome() + "!",
                    "Login riuscito", JOptionPane.INFORMATION_MESSAGE);

            this.dispose();
            new MainGUI(utente).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Username o password errati!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

}