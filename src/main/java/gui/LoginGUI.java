package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    // sotto mettiamo le componenti dell'interfaccia
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registratiButton;

    //Listener esterno per gestire il login
    private LoginListener loginListener;
    
    public LoginGUI(LoginListener loginListener) {
        this.loginListener = loginListener;
        creaInterfaccia();
    }


    private void creaInterfaccia() {
        setTitle("UninaBioGarden - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(34, 139, 34));
        JLabel titleLabel = new JLabel("UninaBioGarden");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        usernameField = new JTextField(15);
        centerPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        passwordField = new JPasswordField(15);
        centerPanel.add(passwordField, gbc);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());

        loginButton = new JButton("Accedi");
        loginButton.setBackground(new Color(34, 139, 34));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loginListener != null) {
                    loginListener.onLogin(usernameFiled.getText(), new Sring(passwordFiled.getPassword()));
                }
            }
        });
        bottomPanel.add(loginButton);

        registratiButton = new JButton("Registrati");
        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loginListener != null) {
                    loginListener.onApriRegistrazione();
                }
            }
        });
        bottomPanel.add(registratiButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Interfaccia per assegnare la logica esterna
    public interface LoginListener {
        void onLogin(String username, String password);
        void onApriRegistrazione();
    }
}
