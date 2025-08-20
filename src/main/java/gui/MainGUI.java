package gui;

import database.*;
import model.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


public class MainGUI extends JFrame {
    private Utente utenteCorrente;
    private LottoDAO lottoDAO;
    private CulturaDAO culturaDAO;
    private ProgettoStagionaleDAO progettoDAO;
    private AttivitaDAO attivitaDAO;
    private RaccoltaDAO raccoltaDAO;
    private UtenteDAO utenteDAO;
    private JPanel contentPanel;
    private JLabel infoUtenteLabel;


    public MainGUI(Utente utente) {
        this.utenteCorrente = utente;
        this.lottoDAO = new LottoDAO();
        this.culturaDAO = new CulturaDAO();
        this.progettoDAO = new ProgettoStagionaleDAO();
        this.attivitaDAO = new AttivitaDAO();
        this.raccoltaDAO = new RaccoltaDAO();
        this.utenteDAO = new UtenteDAO();

        creaInterfaccia();
    }

    private void creaInterfaccia() {
        setTitle(" UninaBioGarden - Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(34, 139, 34));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        infoUtenteLabel = new JLabel(" Benvenuto, " + utenteCorrente.getNome() + " " +
                utenteCorrente.getCognome() + " (" + utenteCorrente.getTipoUtente() + ")");
        infoUtenteLabel.setForeground(Color.WHITE);
        infoUtenteLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(infoUtenteLabel, BorderLayout.WEST);


        JButton logoutButton = new JButton(" Logout");
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setForeground(new Color(34, 139, 34));
        logoutButton.addActionListener(e -> logout());
        headerPanel.add(logoutButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        JPanel menuPanel = creaMenuLaterale();
        add(menuPanel, BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createTitledBorder("Area di lavoro"));
        mostraHome();
        add(contentPanel, BorderLayout.CENTER);
    }


    private JPanel creaMenuLaterale() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createTitledBorder("Menu"));
        menuPanel.setPreferredSize(new Dimension(200, 0));
        menuPanel.setBackground(new Color(240, 240, 240));


        JButton homeButton = creaBottoneMenu(" Home", e -> mostraHome());
        menuPanel.add(homeButton);

        JButton lottiButton = creaBottoneMenu("I miei Lotti", e -> mostraLotti());
        menuPanel.add(lottiButton);

        JButton cultureButton = creaBottoneMenu(" Culture", e -> mostraCulture());
        menuPanel.add(cultureButton);

        JButton progettiButton = creaBottoneMenu("Progetti", e -> mostraProgetti());
        menuPanel.add(progettiButton);

        JButton attivitaButton;
        if ("coltivatore".equals(utenteCorrente.getTipoUtente())) {
            attivitaButton = creaBottoneMenu(" Le mie Attività", e -> mostraAttivita());
        } else {
            attivitaButton = creaBottoneMenu("Gestisci Attività", e -> mostraAttivita());
        }
        menuPanel.add(attivitaButton);

        JButton reportButton = creaBottoneMenu(" Report", e -> mostraReport());
        menuPanel.add(reportButton);


        menuPanel.add(Box.createVerticalGlue());

        return menuPanel;
    }


    private JButton creaBottoneMenu(String testo, ActionListener azione) {
        JButton button = new JButton(testo);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.addActionListener(azione);
        button.setMargin(new Insets(5, 5, 5, 5));
        return button;
    }


    private void mostraHome() {
        contentPanel.removeAll();

        JPanel homePanel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Benvenuto in UninaBioGarden! Seleziona un'opzione dal menu per iniziare." );
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        homePanel.add(welcomeLabel, BorderLayout.NORTH);


        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));


        List<Lotto> lotti = lottoDAO.getLottiByProprietario(utenteCorrente.getUserId());
        JLabel lottiLabel = new JLabel(" Lotti: " + lotti.size(), SwingConstants.CENTER);
        lottiLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statsPanel.add(lottiLabel);

        List<Cultura> culture = culturaDAO.getAllCulture();
        JLabel cultureLabel = new JLabel(" Culture disponibili: " + culture.size(), SwingConstants.CENTER);
        cultureLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statsPanel.add(cultureLabel);

        if ("coltivatore".equals(utenteCorrente.getTipoUtente())) {
            List<Attivita> attivita = attivitaDAO.getAttivitaByColtivatore(utenteCorrente.getUserId());
            JLabel attivitaLabel = new JLabel("Attività totali: " + attivita.size(), SwingConstants.CENTER);
            attivitaLabel.setFont(new Font("Arial", Font.BOLD, 14));
            statsPanel.add(attivitaLabel);
        } else {
            List<ProgettoStagionale> progetti = progettoDAO.getProgettiByProprietario(utenteCorrente.getUserId());
            JLabel progettiLabel = new JLabel(" Progetti: " + progetti.size(), SwingConstants.CENTER);
            progettiLabel.setFont(new Font("Arial", Font.BOLD, 14));
            statsPanel.add(progettiLabel);
        }

        JLabel statoLabel = new JLabel(" Stato: Attivo", SwingConstants.CENTER);
        statoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statsPanel.add(statoLabel);

        homePanel.add(statsPanel, BorderLayout.CENTER);

        contentPanel.add(homePanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }


    private void mostraLotti() {
        contentPanel.removeAll();

        JPanel lottiPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel(" I miei Lotti", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lottiPanel.add(titleLabel, BorderLayout.NORTH);

        List<Lotto> lotti = lottoDAO.getLottiByProprietario(utenteCorrente.getUserId());

        if (lotti.isEmpty()) {
            JLabel noLottiLabel = new JLabel("Non hai ancora nessun lotto. Creane uno!", SwingConstants.CENTER);
            lottiPanel.add(noLottiLabel, BorderLayout.CENTER);
        } else {
            String[] columnNames = {"ID", "Nome Lotto", "Ubicazione", "Dimensione (m²)"};
            Object[][] data = new Object[lotti.size()][4];

            for (int i = 0; i < lotti.size(); i++) {
                Lotto lotto = lotti.get(i);
                data[i][0] = lotto.getLottoId();
                data[i][1] = lotto.getNomeLotto();
                data[i][2] = lotto.getUbicazione();
                data[i][3] = lotto.getDimensione();
            }

            JTable table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            lottiPanel.add(scrollPane, BorderLayout.CENTER);
        }

        JButton addLottoButton = new JButton("Aggiungi Nuovo Lotto");
        addLottoButton.addActionListener(e -> aggiungiLotto());
        lottiPanel.add(addLottoButton, BorderLayout.SOUTH);

        contentPanel.add(lottiPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }


    private void mostraCulture() {
        contentPanel.removeAll();

        JPanel culturePanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel(" Culture Disponibili", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        culturePanel.add(titleLabel, BorderLayout.NORTH);

        List<Cultura> culture = culturaDAO.getAllCulture();

        if (culture.isEmpty()) {
            JLabel noCultureLabel = new JLabel("Nessuna cultura disponibile.", SwingConstants.CENTER);
            culturePanel.add(noCultureLabel, BorderLayout.CENTER);
        } else {
            String[] columnNames = {"ID", "Nome", "Descrizione", "Tempo Maturazione (giorni)"};
            Object[][] data = new Object[culture.size()][4];

            for (int i = 0; i < culture.size(); i++) {
                Cultura cultura = culture.get(i);
                data[i][0] = cultura.getCulturaId();
                data[i][1] = cultura.getNomeCultura();
                data[i][2] = cultura.getDescrizione();
                data[i][3] = cultura.getTempoMaturazione();
            }

            JTable table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            culturePanel.add(scrollPane, BorderLayout.CENTER);
        }

        if ("proprietario".equals(utenteCorrente.getTipoUtente())) {
            JButton addCulturaButton = new JButton("Aggiungi Nuova Cultura");
            addCulturaButton.addActionListener(e -> aggiungiCultura());
            culturePanel.add(addCulturaButton, BorderLayout.SOUTH);
        }

        contentPanel.add(culturePanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }


    private void mostraProgetti() {
        contentPanel.removeAll();

        JPanel progettiPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel(" I miei Progetti Stagionali", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        progettiPanel.add(titleLabel, BorderLayout.NORTH);

        List<ProgettoStagionale> progetti = progettoDAO.getProgettiByProprietario(utenteCorrente.getUserId());

        if (progetti.isEmpty()) {
            JLabel noProgettiLabel = new JLabel("Non hai ancora progetti. Creane uno!", SwingConstants.CENTER);
            progettiPanel.add(noProgettiLabel, BorderLayout.CENTER);
        } else {
            String[] columnNames = {"ID", "Nome Progetto", "Stagione", "Anno", "Stato", "Data Inizio", "Data Fine"};
            Object[][] data = new Object[progetti.size()][7];

            for (int i = 0; i < progetti.size(); i++) {
                ProgettoStagionale progetto = progetti.get(i);
                data[i][0] = progetto.getProgettoId();
                data[i][1] = progetto.getNomeProgetto();
                data[i][2] = progetto.getStagione();
                data[i][3] = progetto.getAnno();
                data[i][4] = progetto.getStato();
                data[i][5] = progetto.getDataInizio();
                data[i][6] = progetto.getDataFine();
            }

            JTable table = new JTable(data, columnNames);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            JScrollPane scrollPane = new JScrollPane(table);
            progettiPanel.add(scrollPane, BorderLayout.CENTER);
        }

        JButton addProgettoButton = new JButton(" Nuovo Progetto Stagionale");
        addProgettoButton.setBackground(new Color(34, 139, 34));
        addProgettoButton.setForeground(Color.WHITE);
        addProgettoButton.addActionListener(e -> aggiungiProgetto());
        progettiPanel.add(addProgettoButton, BorderLayout.SOUTH);

        contentPanel.add(progettiPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }


    private void mostraAttivita() {
        contentPanel.removeAll();

        JPanel attivitaPanel = new JPanel(new BorderLayout());

        String titolo = ("coltivatore".equals(utenteCorrente.getTipoUtente()))
                ? "Le mie Attività"
                : "Gestione Attività";
        JLabel titleLabel = new JLabel(titolo, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        attivitaPanel.add(titleLabel, BorderLayout.NORTH);

        try {
            List<Attivita> attivita;
            if ("coltivatore".equals(utenteCorrente.getTipoUtente())) {
                attivita = attivitaDAO.getAttivitaByColtivatore(utenteCorrente.getUserId());
            } else {
                attivita = getAttivitaDelProrpietario(utenteCorrente.getUserId());
            }

            if (attivita.isEmpty()) {
                JLabel noAttivitaLabel = new JLabel("Non hai ancora attività assegnate. Contatta un proprietario per essere assegnato a un progetto!", SwingConstants.CENTER);
                attivitaPanel.add(noAttivitaLabel, BorderLayout.CENTER);
            } else {
                String[] columnNames = {"ID", "Tipo Attività", "Data Pianificata", "Stato"};
                Object[][] data = new Object[attivita.size()][4];

                for (int i = 0; i < attivita.size(); i++) {
                    Attivita att = attivita.get(i);
                    data[i][0] = att.getAttivitaId();
                    data[i][1] = att.getTipoAttivita();
                    data[i][2] = att.getDataPianificata();
                    data[i][3] = att.getStato();
                }

                JTable table = new JTable(data, columnNames);
                JScrollPane scrollPane = new JScrollPane(table);
                attivitaPanel.add(scrollPane, BorderLayout.CENTER);
            }

            JPanel buttonPanel = new JPanel(new FlowLayout());

            JButton addAttivitaButton = new JButton(" Nuova Attività");
            addAttivitaButton.addActionListener(e -> aggiungiAttivita());
            buttonPanel.add(addAttivitaButton);

            if (!attivita.isEmpty()) {
                JButton cambiaStatoButton = new JButton(" Cambia Stato");
                cambiaStatoButton.addActionListener(e -> cambiaStatoAttivita());
                buttonPanel.add(cambiaStatoButton);
            }

            attivitaPanel.add(buttonPanel, BorderLayout.SOUTH);

        } catch (Exception e) {
            JLabel errorLabel = new JLabel(" Errore nel caricamento attività: " + e.getMessage(), SwingConstants.CENTER);
            attivitaPanel.add(errorLabel, BorderLayout.CENTER);
        }

        contentPanel.add(attivitaPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }


    private void mostraReport() {
        contentPanel.removeAll();

        JPanel reportPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel(" Report e Statistiche", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        reportPanel.add(titleLabel, BorderLayout.NORTH);


        JTextArea reportArea = new JTextArea(20, 50);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        StringBuilder report = new StringBuilder();
        report.append(" REPORT UNINABIOGARDEN \n\n");
        report.append("Utente: ").append(utenteCorrente.getNome()).append(" ").append(utenteCorrente.getCognome()).append("\n");
        report.append("Tipo: ").append(utenteCorrente.getTipoUtente()).append("\n");
        report.append("Data report: ").append(LocalDate.now()).append("\n\n");

        try {
            // dati del report utente
            List<Lotto> lotti = lottoDAO.getLottiByProprietario(utenteCorrente.getUserId());
            report.append(" LOTTI:\n");
            report.append("Numero totale lotti: ").append(lotti.size()).append("\n");
            if (!lotti.isEmpty()) {
                double superficieTotale = lotti.stream().mapToDouble(Lotto::getDimensione).sum();
                report.append("Superficie totale: ").append(String.format("%.2f", superficieTotale)).append(" m²\n");
            }
            report.append("\n");

            List<Cultura> culture = culturaDAO.getAllCulture();
            report.append(" CULTURE:\n");
            report.append("Culture disponibili: ").append(culture.size()).append("\n");
            report.append("\n");

            if ("coltivatore".equals(utenteCorrente.getTipoUtente())) {
                String statsAttivita = attivitaDAO.getStatisticheAttivita(utenteCorrente.getUserId());
                report.append("ATTIVITÀ:\n");
                report.append(statsAttivita).append("\n\n");
            }

        } catch (Exception e) {
            report.append(" Errore nel generare alcune statistiche: ").append(e.getMessage()).append("\n");
        }

        reportArea.setText(report.toString());
        JScrollPane scrollPane = new JScrollPane(reportArea);
        reportPanel.add(scrollPane, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton aggiornaButton = new JButton(" Aggiorna Report");
        aggiornaButton.addActionListener(e -> mostraReport());
        buttonPanel.add(aggiornaButton);


        JButton reportGraficoButton = new JButton(" Report Grafici (JFreeChart)");
        reportGraficoButton.setBackground(new Color(34, 139, 34));
        reportGraficoButton.setForeground(Color.WHITE);
        reportGraficoButton.addActionListener(e -> {
            new ReportGraficoGUI(utenteCorrente).setVisible(true);
        });
        buttonPanel.add(reportGraficoButton);


        reportPanel.add(buttonPanel, BorderLayout.SOUTH);

        contentPanel.add(reportPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }



    private void aggiungiLotto() {
        JTextField nomeField = new JTextField();
        JTextField ubicazioneField = new JTextField();
        JTextField dimensioneField = new JTextField();

        Object[] message = {
                "Nome Lotto:", nomeField,
                "Ubicazione:", ubicazioneField,
                "Dimensione (m²):", dimensioneField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Nuovo Lotto", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String nome = nomeField.getText().trim();
                String ubicazione = ubicazioneField.getText().trim();
                double dimensione = Double.parseDouble(dimensioneField.getText());

                if (nome.isEmpty() || ubicazione.isEmpty()) {
                    JOptionPane.showMessageDialog(this, " Nome e ubicazione sono obbligatori!");
                    return;
                }

                Lotto nuovoLotto = new Lotto(0, nome, ubicazione, dimensione, utenteCorrente);

                if (lottoDAO.inserisciLotto(nuovoLotto)) {
                    JOptionPane.showMessageDialog(this, " Lotto aggiunto con successo!");
                    mostraLotti();
                } else {
                    JOptionPane.showMessageDialog(this, " Errore nell'aggiunta del lotto!");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, " Dimensione non valida!");
            }
        }
    }

    private void aggiungiCultura() {
        JTextField nomeField = new JTextField();
        JTextField descrizioneField = new JTextField();
        JTextField tempoField = new JTextField();

        Object[] message = {
                "Nome Cultura:", nomeField,
                "Descrizione:", descrizioneField,
                "Tempo Maturazione (giorni):", tempoField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Nuova Cultura", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String nome = nomeField.getText().trim();
                String descrizione = descrizioneField.getText().trim();
                int tempo = Integer.parseInt(tempoField.getText());

                if (nome.isEmpty()) {
                    JOptionPane.showMessageDialog(this, " Il nome della cultura è obbligatorio!");
                    return;
                }

                Cultura nuovaCultura = new Cultura(0, nome, descrizione, tempo);

                if (culturaDAO.inserisciCultura(nuovaCultura)) {
                    JOptionPane.showMessageDialog(this, " Cultura aggiunta con successo!");
                    mostraCulture();
                } else {
                    JOptionPane.showMessageDialog(this, " Errore nell'aggiunta della cultura!");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, " Tempo di maturazione non valido!");
            }
        }
    }

    private void aggiungiProgetto() {
        List<Lotto> lotti = lottoDAO.getLottiByProprietario(utenteCorrente.getUserId());
        if (lotti.isEmpty()) {
            JOptionPane.showMessageDialog(this, " Devi prima creare almeno un lotto!");
            return;
        }

        JTextField nomeProgettoField = new JTextField();
        JTextField annoField = new JTextField(String.valueOf(LocalDate.now().getYear()));

        String[] stagioni = {"primavera", "estate", "autunno", "inverno"};
        JComboBox<String> stagioneCombo = new JComboBox<>(stagioni);

        String[] nomiLotti = new String[lotti.size()];
        for (int i = 0; i < lotti.size(); i++) {
            nomiLotti[i] = lotti.get(i).getNomeLotto();
        }
        JComboBox<String> lottoCombo = new JComboBox<>(nomiLotti);

        JTextField dataInizioField = new JTextField(LocalDate.now().toString());
        JTextField dataFineField = new JTextField(LocalDate.now().plusMonths(3).toString());

        Object[] message = {
                "Nome Progetto:", nomeProgettoField,
                "Anno:", annoField,
                "Stagione:", stagioneCombo,
                "Lotto:", lottoCombo,
                "Data Inizio (YYYY-MM-DD):", dataInizioField,
                "Data Fine (YYYY-MM-DD):", dataFineField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Nuovo Progetto", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String nomeProgetto = nomeProgettoField.getText().trim();
                int anno = Integer.parseInt(annoField.getText().trim());
                String stagione = (String) stagioneCombo.getSelectedItem();
                int indiceLotto = lottoCombo.getSelectedIndex();
                int lottoId = lotti.get(indiceLotto).getLottoId();
                LocalDate dataInizio = LocalDate.parse(dataInizioField.getText().trim());
                LocalDate dataFine = LocalDate.parse(dataFineField.getText().trim());

                if (nomeProgetto.isEmpty()) {
                    JOptionPane.showMessageDialog(this, " Il nome del progetto è obbligatorio!");
                    return;
                }

                ProgettoStagionale nuovoProgetto = new ProgettoStagionale(
                        0, nomeProgetto, anno, "pianificato", stagione,
                        dataInizio, dataFine, utenteCorrente.getUserId(), lottoId
                );

                if (progettoDAO.inserisciProgetto(nuovoProgetto)) {
                    JOptionPane.showMessageDialog(this, " Progetto creato con successo!");
                    mostraProgetti();
                } else {
                    JOptionPane.showMessageDialog(this, " Errore nella creazione del progetto!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, " Errore: " + e.getMessage());
            }
        }
    }

    private void aggiungiAttivita() {
        String tipoAttivita = JOptionPane.showInputDialog(this, "Tipo di attività (es. semina, irrigazione, raccolta):");
        if (tipoAttivita == null || tipoAttivita.trim().isEmpty()) {
            return;
        }

        String dataStr = JOptionPane.showInputDialog(this, "Data pianificata (YYYY-MM-DD):", LocalDate.now().toString());
        if (dataStr == null) {
            return;
        }

        String[] stati = {"pianificata", "in_corso", "completata"};
        String stato = (String) JOptionPane.showInputDialog(this,
                "Stato dell'attività:", "Stato",
                JOptionPane.QUESTION_MESSAGE, null, stati, stati[0]);

        if (stato == null) {
            return;
        }

        int coltivatoreAssegnato;

        if("proprietario".equals(utenteCorrente.getTipoUtente())) {
            List<Utente> tuttiGliUtenti = utenteDAO.listaUtenti();
            List<Utente> coltivatori = new ArrayList<>();

            for (Utente utente : tuttiGliUtenti) {
                if ("coltivatore".equals(utente.getTipoUtente())) {
                    coltivatori.add(utente);
                }
            }

            if(coltivatori.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Non ci sononcoltovatori disponibili! \nRegistra prima un coltvatore.");
                return;
            }

            String[] nomiColtivatori = new String[coltivatori.size()];
            for (int i = 0; i < coltivatori.size(); i++) {
                Utente c = coltivatori.get(i);
                nomiColtivatori[i] = c.getNome() + " " + c.getCognome() + " (@" + c.getUsername() + ")";
            }

            String sceltaColtivatore = (String) JOptionPane.showInputDialog(
                    this,
                    "Seleziona il coltivatore da assegnare:",
                            "Assegna Attività",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            nomiColtivatori,
                            nomiColtivatori[0]
            );

            if (sceltaColtivatore == null) {
                return;
            }

            int indiceSelezionato = -1;
            for (int i = 0; i < nomiColtivatori.length; i++) {
                if (nomiColtivatori[i].equals(sceltaColtivatore)) {
                    indiceSelezionato = i;
                    break;
                }
            }
            coltivatoreAssegnato = coltivatori.get(indiceSelezionato).getUserId();

            JOptionPane.showMessageDialog(this,
                    "Attività assegnat a: " + sceltaColtivatore);
        } else {
            coltivatoreAssegnato = utenteCorrente.getUserId();
        }

        try {
            LocalDate data = LocalDate.parse(dataStr);
            Attivita nuovaAttivita = new Attivita(
                    0, stato, coltivatoreAssegnato, data, 1, tipoAttivita.trim()
            );

            if (attivitaDAO.inserisciAttivita(nuovaAttivita)) {
                JOptionPane.showMessageDialog(this, " Attività creata con successo!");
                if ("coltivatore".equals(utenteCorrente.getTipoUtente())) {
                    mostraAttivita();
                }
            } else {
                JOptionPane.showMessageDialog(this, " Errore nella creazione dell'attività!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, " Errore: " + e.getMessage());
        }
    }

    private void cambiaStatoAttivita() {
        List<Attivita> attivita = attivitaDAO.getAttivitaByColtivatore(utenteCorrente.getUserId());

        if (attivita.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Non hai attività da modificare!");
            return;
        }

        String[] nomiAttivita = new String[attivita.size()];
        for (int i = 0; i < attivita.size(); i++) {
            Attivita a = attivita.get(i);
            nomiAttivita[i] = a.getTipoAttivita() + " - " + a.getDataPianificata() + " (Stato: " + a.getStato() + ")";
        }

        String attivitaSelezionata = (String) JOptionPane.showInputDialog(
                this, "Seleziona l'attività:", "Cambia Stato",
                JOptionPane.QUESTION_MESSAGE, null, nomiAttivita, nomiAttivita[0]);

        if (attivitaSelezionata != null) {
            int indice = java.util.Arrays.asList(nomiAttivita).indexOf(attivitaSelezionata);
            Attivita attivita_sel = attivita.get(indice);

            String[] stati = {"pianificata", "in_corso", "completata"};
            String nuovoStato = (String) JOptionPane.showInputDialog(
                    this, "Seleziona il nuovo stato:", "Nuovo Stato",
                    JOptionPane.QUESTION_MESSAGE, null, stati, attivita_sel.getStato());

            if (nuovoStato != null && !nuovoStato.equals(attivita_sel.getStato())) {
                if (attivitaDAO.aggiornaStatoAttivita(attivita_sel.getAttivitaId(), nuovoStato)) {
                    JOptionPane.showMessageDialog(this, " Stato aggiornato con successo!");
                    mostraAttivita();
                } else {
                    JOptionPane.showMessageDialog(this, " Errore nell'aggiornamento dello stato!");
                }
            }
        }
    }

    private void logout() {
        int option = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler uscire?",
                "Logout", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginGUI().setVisible(true);
        }
    }

    private List<Attivita> getAttivitaDelProrpietario(int proprietarioId) {
        List<Attivita> tutteLeAttivita = new ArrayList<>();
        List<ProgettoStagionale> proetti = progettoDAO.getProgettiByProprietario(proprietarioId);

        for (ProgettoStagionale progetto : proetti) {
            List<Attivita> attivitaProgetto = attivitaDAO.getAttivitaByProgetto(progetto.getProgettoId());
            tutteLeAttivita.addAll(attivitaProgetto);
        }

        return tutteLeAttivita;
    }
}