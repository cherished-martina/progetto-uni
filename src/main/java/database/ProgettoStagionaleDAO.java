package database;

import model.ProgettoStagionale;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProgettoStagionaleDAO {
    private Connection connection;

    public ProgettoStagionaleDAO() {
        DatabaseConnection dbConn = new DatabaseConnection();
        this.connection = dbConn.getConnection();
    }

    // INSERIRE UN NUOVO PROGETTO STAGIONALE
    public boolean inserisciProgetto(ProgettoStagionale progetto) {
        String sql = "INSERT INTO progetto_stagionale (nome_progetto, anno, stato, stagione, data_inizio, data_fine, proprietario_id, lotto_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, progetto.getNomeProgetto());
            pstmt.setInt(2, progetto.getAnno());
            pstmt.setString(3, progetto.getStato());
            pstmt.setString(4, progetto.getStagione());
            pstmt.setDate(5, Date.valueOf(progetto.getDataInizio()));
            pstmt.setDate(6, Date.valueOf(progetto.getDataFine()));
            pstmt.setInt(7, progetto.getProprietarioId());
            pstmt.setInt(8, progetto.getLottoId());


            int righeInserite = pstmt.executeUpdate();
            return righeInserite > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'inserimento progetto: " + e.getMessage());
            return false;
        }
    }

    // OTTENERE TUTTI I PROGETTI DI UN PROPRIETARIO
    public List<ProgettoStagionale> getProgettiByProprietario(int proprietarioId) {
        List<ProgettoStagionale> progetti = new ArrayList<>();

        // Query che esclude automaticamente i record con date NULL
        String sql = "SELECT * FROM progetto_stagionale " +
                "WHERE proprietario_id = ? " +
                "AND data_inizio IS NOT NULL " +
                "AND data_fine IS NOT NULL " +
                "ORDER BY progetto_id DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, proprietarioId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                try {
                    // Verifica che i dati siano validi prima di creare l'oggetto
                    String nomeProgetto = rs.getString("nome_progetto");
                    String stagione = rs.getString("stagione");
                    String stato = rs.getString("stato");
                    int anno = rs.getInt("anno");
                    int lottoId = rs.getInt("lotto_id");

                    // analisi delle date
                    java.sql.Date sqlDataInizio = rs.getDate("data_inizio");
                    java.sql.Date sqlDataFine = rs.getDate("data_fine");

                    if (sqlDataInizio != null && sqlDataFine != null) {
                        LocalDate dataInizio = sqlDataInizio.toLocalDate();
                        LocalDate dataFine = sqlDataFine.toLocalDate();

                        ProgettoStagionale progetto = new ProgettoStagionale(
                                rs.getInt("progetto_id"),
                                nomeProgetto != null ? nomeProgetto : "Progetto senza nome",
                                anno,
                                stato != null ? stato : "pianificato",
                                stagione != null ? stagione : "primavera",
                                dataInizio,
                                dataFine,
                                rs.getInt("proprietario_id"),
                                lottoId
                        );
                        progetti.add(progetto);
                    }
                } catch (Exception e) {
                    System.err.println("Errore nel processare progetto ID " + rs.getInt("progetto_id") + ": " + e.getMessage());
                    // Salta questo progetto e continua con il prossimo
                    continue;
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero progetti per proprietario: " + e.getMessage());
            e.printStackTrace();
        }

        return progetti;
    }

    // OTTENERE PROGETTI PER LOTTO
    public List<ProgettoStagionale> getProgettiByLotto(int lottoId) {
        List<ProgettoStagionale> progetti = new ArrayList<>();
        String sql = "SELECT * FROM progetto_stagionale WHERE lotto_id = ? ORDER BY data_inizio DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, lottoId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ProgettoStagionale progetto = new ProgettoStagionale(
                        rs.getInt("progetto_id"),
                        rs.getString("nome_progetto"),
                        rs.getInt("anno"),
                        rs.getString("stato"),
                        rs.getString("stagione"),
                        rs.getDate("data_inizio").toLocalDate(),
                        rs.getDate("data_fine").toLocalDate(),
                        rs.getInt("proprietario_id"),
                        rs.getInt("lottoId")
                );
                progetti.add(progetto);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero progetti per lotto: " + e.getMessage());
        }
        return progetti;
    }

    // OTTENERE UN PROGETTO PER ID
    public ProgettoStagionale getProgettoById(int progettoId) {
        String sql = "SELECT * FROM progetto_stagionale WHERE progetto_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, progettoId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new ProgettoStagionale(
                        rs.getInt("progetto_id"),
                        rs.getString("nome_progetto"),
                        rs.getInt("anno"),
                        rs.getString("stato"),
                        rs.getString("stagione"),
                        rs.getDate("data_inizio").toLocalDate(),
                        rs.getDate("data_fine").toLocalDate(),
                        rs.getInt("proprietario_id"),
                        rs.getInt("lottoId")
                );
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero progetto per ID: " + e.getMessage());
        }
        return null;
    }

    // OTTENERE PROGETTI PER STAGIONE E ANNO
    public List<ProgettoStagionale> getProgettiPerStagioneAnno(String stagione, int anno) {
        List<ProgettoStagionale> progetti = new ArrayList<>();
        String sql = "SELECT * FROM progetto_stagionale WHERE stagione = ? AND anno = ? ORDER BY data_inizio";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, stagione);
            pstmt.setInt(2, anno);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ProgettoStagionale progetto = new ProgettoStagionale(
                        rs.getInt("progetto_id"),
                        rs.getString("nome_progetto"),
                        rs.getInt("anno"),
                        rs.getString("stato"),
                        rs.getString("stagione"),
                        rs.getDate("data_inizio").toLocalDate(),
                        rs.getDate("data_fine").toLocalDate(),
                        rs.getInt("proprietario_id"),
                        rs.getInt("lottoId")
                );
                progetti.add(progetto);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero progetti per stagione/anno: " + e.getMessage());
        }
        return progetti;
    }

    // OTTENERE PROGETTI PER STATO
    public List<ProgettoStagionale> getProgettiPerStato(String stato) {
        List<ProgettoStagionale> progetti = new ArrayList<>();
        String sql = "SELECT * FROM progetto_stagionale WHERE stato = ? ORDER BY data_inizio DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, stato);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ProgettoStagionale progetto = new ProgettoStagionale(
                        rs.getInt("progetto_id"),
                        rs.getString("nome_progetto"),
                        rs.getInt("anno"),
                        rs.getString("stato"),
                        rs.getString("stagione"),
                        rs.getDate("data_inizio").toLocalDate(),
                        rs.getDate("data_fine").toLocalDate(),
                        rs.getInt("proprietario_id"),
                        rs.getInt("lottoId")
                );
                progetti.add(progetto);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero progetti per stato: " + e.getMessage());
        }
        return progetti;
    }

    // AGGIORNARE UN PROGETTO
    public boolean aggiornaProgetto(ProgettoStagionale progetto) {
        String sql = "UPDATE progetto_stagionale SET nome_progetto = ?, anno = ?, stagione = ?, " +
                "data_inizio = ?, data_fine = ?, stato = ? WHERE progetto_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, progetto.getNomeProgetto());
            pstmt.setInt(2, progetto.getAnno());
            pstmt.setString(3, progetto.getStagione());
            pstmt.setDate(4, Date.valueOf(progetto.getDataInizio()));
            pstmt.setDate(5, Date.valueOf(progetto.getDataFine()));
            pstmt.setString(6, progetto.getStato());
            pstmt.setInt(7, progetto.getProgettoId());

            int righeAggiornate = pstmt.executeUpdate();
            return righeAggiornate > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'aggiornamento progetto: " + e.getMessage());
            return false;
        }
    }

    // AGGIORNARE SOLO LO STATO DI UN PROGETTO
    public boolean aggiornaStatoProgetto(int progettoId, String nuovoStato) {
        String sql = "UPDATE progetto_stagionale SET stato = ? WHERE progetto_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nuovoStato);
            pstmt.setInt(2, progettoId);

            int righeAggiornate = pstmt.executeUpdate();
            return righeAggiornate > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'aggiornamento stato progetto: " + e.getMessage());
            return false;
        }
    }

    // ELIMINARE UN PROGETTO
    public boolean eliminaProgetto(int progettoId) {
        String sql = "DELETE FROM progetto_stagionale WHERE progetto_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, progettoId);

            int righeCancellate = pstmt.executeUpdate();
            return righeCancellate > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'eliminazione progetto: " + e.getMessage());
            return false;
        }
    }

    // ASSOCIARE UNA CULTURA A UN PROGETTO (tabella progetto_cultura)
    public boolean associaCulturaProgetto(int progettoId, int culturaId) {
        String sql = "INSERT INTO progetto_cultura (progetto_id, cultura_id) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, progettoId);
            pstmt.setInt(2, culturaId);

            int righeInserite = pstmt.executeUpdate();
            return righeInserite > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'associazione cultura-progetto: " + e.getMessage());
            return false;
        }
    }

    // RIMUOVERE ASSOCIAZIONE CULTURA-PROGETTO
    public boolean rimuoviCulturaProgetto(int progettoId, int culturaId) {
        String sql = "DELETE FROM progetto_cultura WHERE progetto_id = ? AND cultura_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, progettoId);
            pstmt.setInt(2, culturaId);

            int righeCancellate = pstmt.executeUpdate();
            return righeCancellate > 0;

        } catch (SQLException e) {
            System.err.println("Errore nella rimozione associazione cultura-progetto: " + e.getMessage());
            return false;
        }
    }

    // OTTENERE PROGETTI CON INFORMAZIONI COMPLETE (JOIN con lotto e utente)
    public List<String> getProgettiCompleti(int proprietarioId) {
        List<String> progetti = new ArrayList<>();
        String sql = "SELECT ps.*, l.nome_lotto, u.nome as nome_proprietario, u.cognome " +
                "FROM progetto_stagionale ps " +
                "JOIN lotto l ON ps.lotto_id = l.lotto_id " +
                "JOIN utente u ON ps.proprietario_id = u.user_id " +
                "WHERE ps.proprietario_id = ? " +
                "ORDER BY ps.data_inizio DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, proprietarioId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String infoProgetto = String.format(
                        "Progetto: %s | Stagione: %s %d | Lotto: %s | Stato: %s | Dal %s al %s",
                        rs.getString("nome_progetto"),
                        rs.getString("stagione"),
                        rs.getInt("anno"),
                        rs.getString("nome_lotto"),
                        rs.getString("stato"),
                        rs.getDate("data_inizio").toString(),
                        rs.getDate("data_fine").toString()
                );
                progetti.add(infoProgetto);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero progetti completi: " + e.getMessage());
        }
        return progetti;
    }

    // VERIFICARE SE ESISTE GIÀ UN PROGETTO PER STAGIONE/ANNO/LOTTO
    public boolean esisteProgettoPerStagioneLotto(String stagione, int anno, int lottoId) {
        String sql = "SELECT COUNT(*) FROM progetto_stagionale WHERE stagione = ? AND anno = ? AND lotto_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, stagione);
            pstmt.setInt(2, anno);
            pstmt.setInt(3, lottoId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Errore nella verifica esistenza progetto: " + e.getMessage());
        }
        return false;
    }
}