package gui; //GUI Graphical User Interface

import controller.UtenteController;
import model.Utente;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrazioneGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField emailField;
    private JComboBox<String> tipoUtenteCombo;
    private JButton registratiButton;
    private JButton annullaButton;
    private UtenteController utenteController;

    public RegistrazioneGUI() {
        this.utenteController = new UtenteController();
        creaInterfaccia();
    }

    private void creaInterfaccia() {
        // === IMPOSTAZIONI FINESTRA ===
        setTitle("📝 Registrazione - UninaBioGarden");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chiude solo questa finestra
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === TITOLO ===
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(34, 139, 34));
        JLabel titleLabel = new JLabel("📝 Crea il tuo account");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        // === FORM DI REGISTRAZIONE ===
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        nomeField = new JTextField(15);
        centerPanel.add(nomeField, gbc);

        // Cognome
        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(new JLabel("Cognome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        cognomeField = new JTextField(15);
        centerPanel.add(cognomeField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        emailField = new JTextField(15);
        centerPanel.add(emailField, gbc);

        // Username
        gbc.gridx = 0; gbc.gridy = 3;
        centerPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        usernameField = new JTextField(15);
        centerPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 4;
        centerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        passwordField = new JPasswordField(15);
        centerPanel.add(passwordField, gbc);

        // Tipo Utente
        gbc.gridx = 0; gbc.gridy = 5;
        centerPanel.add(new JLabel("Tipo Utente:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        tipoUtenteCombo = new JComboBox<>(new String[]{"proprietario", "coltivatore"});
        centerPanel.add(tipoUtenteCombo, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // === BOTTONI ===
        JPanel bottomPanel = new JPanel(new FlowLayout());

        // Bottone Registrati
        registratiButton = new JButton("✅ Registrati");
        registratiButton.setBackground(new Color(34, 139, 34));
        registratiButton.setForeground(Color.WHITE);
        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eseguiRegistrazione();
            }
        });
        bottomPanel.add(registratiButton);

        // Bottone Annulla
        annullaButton = new JButton("❌ Annulla");
        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Chiude questa finestra
            }
        });
        bottomPanel.add(annullaButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void eseguiRegistrazione() {
        String nome = nomeField.getText().trim();
        String cognome = cognomeField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String tipoUtente = (String) tipoUtenteCombo.getSelectedItem();

        if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() ||
                username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tutti i campi sono obbligatori!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (utenteController.esisteUsername(username)) {
            JOptionPane.showMessageDialog(this, "Username già esistente! Scegline un altro.",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Utente nuovoUtente = new Utente(nome, cognome, email, password, tipoUtente, username);

        if (utenteController.registraUtente(nuovoUtente)) {
            JOptionPane.showMessageDialog(this,
                    "Registrazione completata con successo!\nOra puoi fare il login.",
                    "Successo", JOptionPane.INFORMATION_MESSAGE);

            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Errore durante la registrazione. Riprova.",
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}
