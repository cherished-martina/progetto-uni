package gui;

import database.UtenteDAO;
import model.Utente;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    // Componenti dell'interfaccia
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registratiButton;
    private UtenteDAO utenteDAO;

    // COSTRUTTORE - Si esegue quando si crea la finestra
    public LoginGUI() {
        this.utenteDAO = new UtenteDAO(); // Connessione al database
        creaInterfaccia(); // Metodo che disegna la finestra
    }

    // METODO che crea tutti i componenti grafici
    private void creaInterfaccia() {
        // === IMPOSTAZIONI FINESTRA ===
        setTitle("🌱 UninaBioGarden - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centro dello schermo
        setLayout(new BorderLayout());

        // === PANNELLO SUPERIORE (Titolo) ===
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(34, 139, 34)); // Verde
        JLabel titleLabel = new JLabel("🌱 UninaBioGarden");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        // === PANNELLO CENTRALE (Form di login) ===
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spaziatura

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        usernameField = new JTextField(15);
        centerPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        passwordField = new JPasswordField(15);
        centerPanel.add(passwordField, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // === PANNELLO INFERIORE (Bottoni) ===
        JPanel bottomPanel = new JPanel(new FlowLayout());

        // Bottone Login
        loginButton = new JButton("🔑 Accedi");
        loginButton.setBackground(new Color(34, 139, 34));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eseguiLogin(); // Chiama il metodo di login
            }
        });
        bottomPanel.add(loginButton);

        // Bottone Registrati
        registratiButton = new JButton("📝 Registrati");
        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apriRegistrazione(); // Chiama il metodo di registrazione
            }
        });
        bottomPanel.add(registratiButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // METODO che gestisce il login
    private void eseguiLogin() {
        // Prendi i dati dai campi
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Controlla che non siano vuoti
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Inserisci username e password!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Prova a fare il login usando il DAO
        Utente utente = utenteDAO.autenticaUtente(username, password);

        if (utente != null) {
            // LOGIN RIUSCITO!
            JOptionPane.showMessageDialog(this,
                    "✅ Benvenuto, " + utente.getNome() + " " + utente.getCognome() + "!",
                    "Login riuscito", JOptionPane.INFORMATION_MESSAGE);

            // Chiudi questa finestra e apri la main
            this.dispose();
            new MainGUI(utente).setVisible(true);
        } else {
            // LOGIN FALLITO
            JOptionPane.showMessageDialog(this, "❌ Username o password errati!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // METODO che apre la finestra di registrazione
    private void apriRegistrazione() {
        new RegistrazioneGUI().setVisible(true);
    }

    // METODO MAIN - FA PARTIRE L'APPLICAZIONE
    public static void main(String[] args) {
        // Imposta il look and feel del sistema
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crea e mostra la finestra di login
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginGUI().setVisible(true);
            }
        });
    }
}