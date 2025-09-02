package gui;

import controller.*;
import model.*;

// Import JFreeChart
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReportGraficoGUI extends JFrame {
    private Utente utenteCorrente;
    private LottoController lottoController;
    private AttivitaController attivitaController;
    private CulturaController culturaController;
    private ProgettoStagionaleController progettoStagionaleController;
    private JPanel chartPanel;
    private JComboBox<String> tipoReportCombo;

    public ReportGraficoGUI(Utente utente) {
        this.utenteCorrente = utente;
        this.lottoController = new LottoController();
        this.attivitaController = new AttivitaController();
        this.culturaController = new CulturaController();
        this.progettoStagionaleController = new ProgettoStagionaleController();

        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Report Grafici JFreeChart - UninaBioGarden");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(new Color(34, 139, 34));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Report Grafici con JFreeChart:");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        String[] tipiReport = {
                "Grafico Lotti (Barre)",
                "Distribuzione Culture (Torta)",
                "Stato Attività (Barre)"
        };
        tipoReportCombo = new JComboBox<>(tipiReport);
        tipoReportCombo.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton generaButton = new JButton("Genera Grafico");
        generaButton.setBackground(Color.WHITE);
        generaButton.setForeground(new Color(34, 139, 34));
        generaButton.setFont(new Font("Arial", Font.BOLD, 14));
        generaButton.addActionListener(e -> generaReportSelezionato());

        topPanel.add(titleLabel);
        topPanel.add(Box.createHorizontalStrut(15));
        topPanel.add(tipoReportCombo);
        topPanel.add(Box.createHorizontalStrut(15));
        topPanel.add(generaButton);
        add(topPanel, BorderLayout.NORTH);


        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("Grafico JFreeChart"));

        JLabel msgIniziale = new JLabel(
                " Report Grafici con JFreeChart - Seleziona un tipo di grafico e clicca 'Genera Grafico'" +
                        "Grafici disponibili" +
                        "Dimensioni lotti (grafico a barre)" +
                        "Distribuzione culture (grafico a torta)" +
                        "Stato attività/progetti (grafico a barre)",
                SwingConstants.CENTER
        );
        msgIniziale.setFont(new Font("Arial", Font.PLAIN, 13));
        chartPanel.add(msgIniziale, BorderLayout.CENTER);

        add(chartPanel, BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel infoLabel = new JLabel("Powered by JFreeChart Library");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setForeground(Color.GRAY);
        bottomPanel.add(infoLabel);

        bottomPanel.add(Box.createHorizontalStrut(50));

        JButton chiudiButton = new JButton("Chiudi");
        chiudiButton.setFont(new Font("Arial", Font.PLAIN, 14));
        chiudiButton.addActionListener(e -> dispose());
        bottomPanel.add(chiudiButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void generaReportSelezionato() {
        String tipoSelezionato = (String) tipoReportCombo.getSelectedItem();

        chartPanel.removeAll();

        try {
            JFreeChart chart = null;

            if (tipoSelezionato.contains("Lotti")) {
                chart = creaGraficoLotti();
            } else if (tipoSelezionato.contains("Culture")) {
                chart = creaGraficoCulture();
            } else if (tipoSelezionato.contains("Attività")) {
                chart = creaGraficoStatoAttivita();
            }

            if (chart != null) {
                ChartPanel panel = new ChartPanel(chart);
                panel.setPreferredSize(new Dimension(800, 450));
                chartPanel.add(panel, BorderLayout.CENTER);


                JTextArea infoArea = new JTextArea(4, 80);
                infoArea.setEditable(false);
                infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                infoArea.setBackground(new Color(248, 248, 248));
                infoArea.setText(generaInfoGrafico(tipoSelezionato));
                JScrollPane infoScroll = new JScrollPane(infoArea);
                infoScroll.setPreferredSize(new Dimension(800, 100));
                chartPanel.add(infoScroll, BorderLayout.SOUTH);
            }

        } catch (Exception e) {
            JPanel errorPanel = new JPanel(new BorderLayout());
            JLabel errorLabel = new JLabel(
                    "Errore nella generazione del grafico" + e.getMessage(),
                    SwingConstants.CENTER
            );
            errorPanel.add(errorLabel, BorderLayout.CENTER);
            chartPanel.add(errorPanel, BorderLayout.CENTER);
            e.printStackTrace();
        }

        chartPanel.revalidate();
        chartPanel.repaint();
    }


    private JFreeChart creaGraficoLotti() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<Lotto> lotti = lottoController.getLottiByProprietario(utenteCorrente.getUserId());

        if (lotti.isEmpty()) {
            dataset.addValue(0, "Dimensione", "Nessun lotto");
        } else {
            for (Lotto lotto : lotti) {
                dataset.addValue(lotto.getDimensione(), "Dimensione (m²)", lotto.getNomeLotto());
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Dimensioni Lotti di " + utenteCorrente.getNome(),    // titolo del grafico
                "Lotti",                                                // cosa c'è scritto sotto (asse X)
                "Dimensione (m²)",                                      // cosa a sinistra (asse Y)
                dataset,                                                // i dati
                PlotOrientation.VERTICAL,                               // le barre
                true,                                                   // con legenda
                true,                                                   // info mouse (tooltips)
                false                                                   // non cliccabile (URLs)
        );


        chart.getPlot().setBackgroundPaint(new Color(240, 255, 240));
        chart.setBackgroundPaint(Color.WHITE);

        return chart;
    }


    private JFreeChart creaGraficoCulture() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        List<Cultura> culture = culturaController.getAllCultura();

        if (culture.isEmpty()) {
            dataset.setValue("Nessuna cultura", 1);
        } else {
            for (Cultura cultura : culture) {
                double utilizzo = Math.max(5, 120 - cultura.getTempoMaturazione());
                dataset.setValue(cultura.getNomeCultura(), utilizzo);
            }
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Distribuzione Utilizzo Culture",     // titolo del report
                dataset,                                 // i dati
                true,                                    // legenda
                true,                                    // mouse (tooltips)
                false                                    // URLs
        );

        chart.setBackgroundPaint(Color.WHITE);
        return chart;
    }


    private JFreeChart creaGraficoStatoAttivita() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String titoloGrafico = "";

        if ("coltivatore".equals(utenteCorrente.getTipoUtente())) {
            List<Attivita> attivita = attivitaController.getAttivitaByColtivatore(utenteCorrente.getUserId());

            int pianificate = 0, inCorso = 0, completate = 0;

            for (Attivita a : attivita) {
                String stato = a.getStato();
                if (stato != null) {
                    switch (stato) {
                        case "pianificata": pianificate++; break;
                        case "in_corso": inCorso++; break;
                        case "completata": completate++; break;
                    }
                }
            }

            dataset.addValue(pianificate, "Attività", "Pianificate");
            dataset.addValue(inCorso, "Attività", "In Corso");
            dataset.addValue(completate, "Attività", "Completate");

            titoloGrafico = "Stato Attività di " + utenteCorrente.getNome();

        } else {
            List<ProgettoStagionale> progetti = progettoStagionaleController.getProgettiByProprietario(utenteCorrente.getUserId());

            int pianificati = 0, inCorso = 0, completati = 0;

            for (ProgettoStagionale p : progetti) {
                String stato = p.getStato();
                if (stato != null) {
                    switch (stato) {
                        case "pianificato": pianificati++; break;
                        case "in_corso": inCorso++; break;
                        case "completato": completati++; break;
                    }
                }
            }

            dataset.addValue(pianificati, "Progetti", "Pianificati");
            dataset.addValue(inCorso, "Progetti", "In Corso");
            dataset.addValue(completati, "Progetti", "Completati");

            titoloGrafico = "Stato Progetti di " + utenteCorrente.getNome();
        }

        JFreeChart chart = ChartFactory.createBarChart(
                titoloGrafico,                          // titolo/dati/leggenda ecc come sopra
                "Stati",
                "Numero",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        chart.getPlot().setBackgroundPaint(new Color(255, 248, 240));
        chart.setBackgroundPaint(Color.WHITE);

        return chart;
    }


    private String generaInfoGrafico(String tipoGrafico) {
        StringBuilder info = new StringBuilder();
        info.append("STATISTICHE DETTAGLIATE\n");
        info.append("=" + "=".repeat(50) + "\n");

        try {
            if (tipoGrafico.contains("Lotti")) {
                List<Lotto> lotti = lottoController.getLottiByProprietario(utenteCorrente.getUserId());
                double totale = lotti.stream().mapToDouble(Lotto::getDimensione).sum();
                double media = lotti.isEmpty() ? 0 : totale / lotti.size();

                info.append("ANALISI LOTTI:\n");
                info.append("• Numero lotti: ").append(lotti.size()).append("\n");
                info.append("• Superficie totale: ").append(String.format("%.2f m²", totale)).append("\n");
                info.append("• Superficie media per lotto: ").append(String.format("%.2f m²", media)).append("\n");

                if (!lotti.isEmpty()) {
                    Lotto maggiore = lotti.stream().max((l1, l2) -> Double.compare(l1.getDimensione(), l2.getDimensione())).get();
                    info.append("• Lotto più grande: ").append(maggiore.getNomeLotto())
                            .append(" (").append(String.format("%.2f m²", maggiore.getDimensione())).append(")\n");
                }

            } else if (tipoGrafico.contains("Culture")) {
                List<Cultura> culture = culturaController.getAllCultura();
                info.append("ANALISI CULTURE:\n");
                info.append("• Culture disponibili: ").append(culture.size()).append("\n");

                if (!culture.isEmpty()) {
                    double tempoMedio = culture.stream().mapToInt(Cultura::getTempoMaturazione).average().orElse(0);
                    info.append("Tempo maturazione medio: ").append(String.format("%.1f giorni", tempoMedio)).append("\n");

                    Cultura veloce = culture.stream().min((c1, c2) -> Integer.compare(c1.getTempoMaturazione(), c2.getTempoMaturazione())).get();
                    Cultura lenta = culture.stream().max((c1, c2) -> Integer.compare(c1.getTempoMaturazione(), c2.getTempoMaturazione())).get();

                    info.append("Cultura più veloce: ").append(veloce.getNomeCultura())
                            .append(" (").append(veloce.getTempoMaturazione()).append(" giorni)\n");
                    info.append("Cultura più lenta: ").append(lenta.getNomeCultura())
                            .append(" (").append(lenta.getTempoMaturazione()).append(" giorni)\n");
                }

            } else if (tipoGrafico.contains("Attività")) {
                if ("coltivatore".equals(utenteCorrente.getTipoUtente())) {
                    List<Attivita> attivita = attivitaController.getAttivitaByColtivatore(utenteCorrente.getUserId());
                    long completate = attivita.stream().filter(a -> "completata".equals(a.getStato())).count();

                    info.append("ANALISI ATTIVITÀ:\n");
                    info.append("Attività totali: ").append(attivita.size()).append("\n");
                    info.append("Attività completate: ").append(completate).append("\n");

                    if (attivita.size() > 0) {
                        double percentuale = (completate * 100.0) / attivita.size();
                        info.append("Percentuale completamento: ").append(String.format("%.1f%%", percentuale)).append("\n");

                        String valutazione;
                        if (percentuale >= 80) valutazione = "Eccellente! ";
                        else if (percentuale >= 60) valutazione = "Buono ";
                        else if (percentuale >= 40) valutazione = "Sufficiente ";
                        else valutazione = "Da migliorare ";

                        info.append("Valutazione: ").append(valutazione).append("\n");
                    }
                } else {
                    List<ProgettoStagionale> progetti = progettoStagionaleController.getProgettiByProprietario(utenteCorrente.getUserId());
                    long completati = progetti.stream().filter(p -> "completato".equals(p.getStato())).count();

                    info.append("ANALISI PROGETTI:\n");
                    info.append("Progetti totali: ").append(progetti.size()).append("\n");
                    info.append("Progetti completati: ").append(completati).append("\n");

                    if (progetti.size() > 0) {
                        double percentuale = (completati * 100.0) / progetti.size();
                        info.append("Percentuale completamento: ").append(String.format("%.1f%%", percentuale)).append("\n");
                    }
                }
            }

        } catch (Exception e) {
            info.append("Errore nel calcolo delle statistiche: ").append(e.getMessage());
        }

        info.append("\n Report generato: ").append(java.time.LocalDateTime.now().toString().substring(0, 19));
        info.append(" |  Utente: ").append(utenteCorrente.getUsername());

        return info.toString();
    }
}