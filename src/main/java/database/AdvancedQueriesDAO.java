package database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdvancedQueriesDAO {
    private Connection connection;

    public AdvancedQueriesDAO() {
        DatabaseConnection dbConn = new DatabaseConnection();
        this.connection = dbConn.getConnection();
    }




    public List<Map<String, Object>> getAttivitaCompleteConUrgenza(int coltivatoreId) {
        List<Map<String, Object>> risultati = new ArrayList<>();
        String sql = "SELECT * FROM vista_attivita_complete WHERE coltivatore_id = ? ORDER BY urgenza, data_pianificata";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, coltivatoreId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> attivita = new HashMap<>();
                attivita.put("attivita_id", rs.getInt("attivita_id"));
                attivita.put("tipo_attivita", rs.getString("tipo_attivita"));
                attivita.put("data_pianificata", rs.getDate("data_pianificata"));
                attivita.put("stato", rs.getString("stato"));
                attivita.put("nome_progetto", rs.getString("nome_progetto"));
                attivita.put("nome_lotto", rs.getString("nome_lotto"));
                attivita.put("urgenza", rs.getString("urgenza"));
                risultati.add(attivita);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero attività con urgenza: " + e.getMessage());
        }

        return risultati;
    }


    public List<Map<String, Object>> getStatisticheProgetti(int proprietarioId) {
        List<Map<String, Object>> risultati = new ArrayList<>();
        String sql = "SELECT * FROM vista_progetti_statistiche WHERE proprietario_id = ? ORDER BY percentuale_completamento DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, proprietarioId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> progetto = new HashMap<>();
                progetto.put("progetto_id", rs.getInt("progetto_id"));
                progetto.put("nome_progetto", rs.getString("nome_progetto"));
                progetto.put("stagione", rs.getString("stagione"));
                progetto.put("anno", rs.getInt("anno"));
                progetto.put("stato", rs.getString("stato"));
                progetto.put("nome_lotto", rs.getString("nome_lotto"));
                progetto.put("numero_attivita", rs.getInt("numero_attivita"));
                progetto.put("attivita_completate", rs.getInt("attivita_completate"));
                progetto.put("numero_culture", rs.getInt("numero_culture"));
                progetto.put("numero_raccolte", rs.getInt("numero_raccolte"));
                progetto.put("quantita_totale_raccolta", rs.getDouble("quantita_totale_raccolta"));
                progetto.put("percentuale_completamento", rs.getDouble("percentuale_completamento"));
                risultati.add(progetto);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero statistiche progetti: " + e.getMessage());
        }

        return risultati;
    }




    public List<Map<String, Object>> calcolaProduttivitaStagionale(int lottoId, String stagione, int anno) {
        List<Map<String, Object>> risultati = new ArrayList<>();
        String sql = "SELECT * FROM calcola_produttivita_stagionale(?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, lottoId);
            pstmt.setString(2, stagione);
            pstmt.setInt(3, anno);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> produttivita = new HashMap<>();
                produttivita.put("cultura_nome", rs.getString("cultura_nome"));
                produttivita.put("quantita_raccolta", rs.getDouble("quantita_raccolta"));
                produttivita.put("produttivita_per_mq", rs.getDouble("produttivita_per_mq"));
                produttivita.put("numero_raccolte", rs.getLong("numero_raccolte"));
                risultati.add(produttivita);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel calcolo produttività stagionale: " + e.getMessage());
        }

        return risultati;
    }



    public List<Map<String, Object>> suggerisciCulturePerStagione(String stagione) {
        List<Map<String, Object>> risultati = new ArrayList<>();
        String sql = "SELECT * FROM suggerisci_culture_per_stagione(?) LIMIT 10";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, stagione);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> cultura = new HashMap<>();
                cultura.put("cultura_id", rs.getInt("cultura_id"));
                cultura.put("nome_cultura", rs.getString("nome_cultura"));
                cultura.put("tempo_maturazione", rs.getInt("tempo_maturazione"));
                cultura.put("produttivita_media", rs.getDouble("produttivita_media"));
                cultura.put("numero_utilizzi", rs.getLong("numero_utilizzi"));
                cultura.put("punteggio_raccomandazione", rs.getDouble("punteggio_raccomandazione"));
                risultati.add(cultura);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel suggerimento culture: " + e.getMessage());
        }

        return risultati;
    }


    public List<Map<String, Object>> analizzaPerformanceColtivatore(int coltivatoreId) {
        List<Map<String, Object>> risultati = new ArrayList<>();
        String sql = "SELECT * FROM analizza_performance_coltivatore(?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, coltivatoreId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> performance = new HashMap<>();
                performance.put("periodo", rs.getString("periodo"));
                performance.put("attivita_assegnate", rs.getLong("attivita_assegnate"));
                performance.put("attivita_completate", rs.getLong("attivita_completate"));
                performance.put("attivita_in_ritardo", rs.getLong("attivita_in_ritardo"));
                performance.put("percentuale_completamento", rs.getDouble("percentuale_completamento"));
                performance.put("puntualita_media", rs.getDouble("puntualita_media"));
                performance.put("valutazione", rs.getString("valutazione"));
                risultati.add(performance);
            }
        } catch (SQLException e) {
            System.err.println("Errore nell'analisi performance: " + e.getMessage());
        }

        return risultati;
    }



    public List<Map<String, Object>> prevediRaccolteFuture(int proprietarioId) {
        List<Map<String, Object>> risultati = new ArrayList<>();
        String sql = "SELECT * FROM prevedi_raccolte_future(?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, proprietarioId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> previsione = new HashMap<>();
                previsione.put("progetto_nome", rs.getString("progetto_nome"));
                previsione.put("cultura_nome", rs.getString("cultura_nome"));
                previsione.put("data_semina_stimata", rs.getDate("data_semina_stimata"));
                previsione.put("data_raccolta_prevista", rs.getDate("data_raccolta_prevista"));
                previsione.put("giorni_alla_raccolta", rs.getInt("giorni_alla_raccolta"));
                previsione.put("quantita_stimata", rs.getDouble("quantita_stimata"));
                risultati.add(previsione);
            }
        } catch (SQLException e) {
            System.err.println("Errore nelle previsioni raccolte: " + e.getMessage());
        }

        return risultati;
    }


    public Map<String, Object> getStatisticheCompleteUtente(int userId) {
        Map<String, Object> statistiche = new HashMap<>();
        String sql = "SELECT * FROM get_statistiche_utente(?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                statistiche.put("numero_lotti", rs.getInt("numero_lotti"));
                statistiche.put("superficie_totale", rs.getDouble("superficie_totale"));
                statistiche.put("numero_progetti", rs.getInt("numero_progetti"));
                statistiche.put("progetti_completati", rs.getInt("progetti_completati"));
                statistiche.put("numero_culture_usate", rs.getInt("numero_culture_usate"));
                statistiche.put("raccolte_totali", rs.getInt("raccolte_totali"));
                statistiche.put("quantita_raccolta_totale", rs.getDouble("quantita_raccolta_totale"));
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero statistiche utente: " + e.getMessage());
        }

        return statistiche;
    }



    public Map<String, Double> getDatiGraficoRaccolteLotto(int proprietarioId) {
        Map<String, Double> dati = new HashMap<>();
        String sql = "SELECT * FROM get_report_raccolte_lotto(?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, proprietarioId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String nomeLotto = rs.getString("nome_lotto");
                double quantitaTotale = rs.getDouble("quantita_totale");
                dati.put(nomeLotto, quantitaTotale);
            }
        } catch (SQLException e) {
            System.err.println("Errore nei dati grafico raccolte: " + e.getMessage());
        }

        return dati;
    }


    public Map<String, Integer> getDatiGraficoCulture() {
        Map<String, Integer> dati = new HashMap<>();
        String sql = "SELECT c.nome_cultura, COUNT(pc.progetto_id)::INTEGER as utilizzi " +
                "FROM cultura c " +
                "LEFT JOIN progetto_cultura pc ON c.cultura_id = pc.cultura_id " +
                "GROUP BY c.cultura_id, c.nome_cultura " +
                "ORDER BY utilizzi DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String nomeCultura = rs.getString("nome_cultura");
                int utilizzi = rs.getInt("utilizzi");
                dati.put(nomeCultura, utilizzi);
            }
        } catch (SQLException e) {
            System.err.println("Errore nei dati grafico culture: " + e.getMessage());
        }

        return dati;
    }



    public Map<String, Integer> getDatiGraficoStatoAttivita(int coltivatoreId) {
        Map<String, Integer> dati = new HashMap<>();
        String sql = "SELECT stato, COUNT(*)::INTEGER as numero " +
                "FROM attivita WHERE coltivatore_id = ? " +
                "GROUP BY stato";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, coltivatoreId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String stato = rs.getString("stato");
                int numero = rs.getInt("numero");
                dati.put(stato, numero);
            }
        } catch (SQLException e) {
            System.err.println("Errore nei dati grafico stato attività: " + e.getMessage());
        }

        return dati;
    }




    public String eseguiPuliziaDati(int giorniRetention) {
        String risultato = "Nessuna operazione eseguita";
        String sql = "SELECT pulizia_dati_obsoleti(?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, giorniRetention);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                risultato = rs.getString(1);
            }
        } catch (SQLException e) {
            risultato = "Errore nella pulizia: " + e.getMessage();
        }

        return risultato;
    }



    public boolean aggiornaStatiProgetti() {
        String sql = "INSERT INTO aggiornamenti_giornalieri (data_aggiornamento) VALUES (CURRENT_DATE) ON CONFLICT DO NOTHING";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int righeInserite = pstmt.executeUpdate();
            return righeInserite > 0;
        } catch (SQLException e) {
            System.err.println("Errore nell'aggiornamento stati progetti: " + e.getMessage());
            return false;
        }
    }


    public List<Map<String, Object>> getLogModificheRecenti(int limite) {
        List<Map<String, Object>> log = new ArrayList<>();
        String sql = "SELECT * FROM log_modifiche ORDER BY data_modifica DESC LIMIT ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, limite);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("log_id", rs.getInt("log_id"));
                entry.put("tabella_modificata", rs.getString("tabella_modificata"));
                entry.put("operazione", rs.getString("operazione"));
                entry.put("record_id", rs.getInt("record_id"));
                entry.put("data_modifica", rs.getTimestamp("data_modifica"));
                entry.put("valori_vecchi", rs.getString("valori_vecchi"));
                entry.put("valori_nuovi", rs.getString("valori_nuovi"));
                log.add(entry);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero log modifiche: " + e.getMessage());
        }

        return log;
    }



    public String generaReportCompletoUtente(int userId) {
        StringBuilder report = new StringBuilder();

        try {

            Map<String, Object> stats = getStatisticheCompleteUtente(userId);
            report.append("=== REPORT COMPLETO UTENTE ===\n\n");
            report.append("📊 STATISTICHE GENERALI:\n");
            report.append("• Lotti: ").append(stats.get("numero_lotti")).append("\n");
            report.append("• Superficie totale: ").append(stats.get("superficie_totale")).append(" m²\n");
            report.append("• Progetti: ").append(stats.get("numero_progetti")).append("\n");
            report.append("• Progetti completati: ").append(stats.get("progetti_completati")).append("\n");
            report.append("• Culture diverse utilizzate: ").append(stats.get("numero_culture_usate")).append("\n");
            report.append("• Raccolte totali: ").append(stats.get("raccolte_totali")).append("\n");
            report.append("• Quantità raccolta totale: ").append(stats.get("quantita_raccolta_totale")).append(" kg\n\n");


            List<Map<String, Object>> previsioni = prevediRaccolteFuture(userId);
            if (!previsioni.isEmpty()) {
                report.append("🔮 PREVISIONI RACCOLTE FUTURE:\n");
                for (Map<String, Object> p : previsioni) {
                    report.append(String.format("• %s (%s): %s (tra %d giorni)\n",
                            p.get("progetto_nome"),
                            p.get("cultura_nome"),
                            p.get("data_raccolta_prevista"),
                            p.get("giorni_alla_raccolta")));
                }
                report.append("\n");
            }

            report.append("📅 Report generato il: ").append(LocalDate.now()).append("\n");

        } catch (Exception e) {
            report.append("❌ Errore nella generazione del report: ").append(e.getMessage());
        }

        return report.toString();
    }
}