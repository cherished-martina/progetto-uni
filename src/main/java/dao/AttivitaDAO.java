package dao;

import model.Attivita;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttivitaDAO {
    private Connection connection;

    public AttivitaDAO() {
        DatabaseConnection dbConn = new DatabaseConnection();
        this.connection = dbConn.getConnection();
    }


    public boolean inserisciAttivita(Attivita attivita) {
        String sql = "INSERT INTO attivita ( stato, coltivatore_id, data_pianificata, progetto_id, tipo_attivita) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, attivita.getStato());
            pstmt.setInt(2, attivita.getColtivatoreId());
            pstmt.setDate(3, Date.valueOf(attivita.getDataPianificata()));
            pstmt.setInt(4, attivita.getProgettoId());
            pstmt.setString(5, attivita.getTipoAttivita());


            int righeInserite = pstmt.executeUpdate();
            return righeInserite > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'inserimento attività: " + e.getMessage());
            return false;
        }
    }


    public List<Attivita> getAttivitaByColtivatore(int coltivatoreId) {
        List<Attivita> attivita = new ArrayList<>();
        String sql = "SELECT * FROM attivita WHERE coltivatore_id = ? ORDER BY data_pianificata";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, coltivatoreId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Attivita att = new Attivita(
                        rs.getInt("attivita_id"),
                        rs.getString("stato"),
                        rs.getInt("coltivatore_id"),
                        rs.getDate("data_pianificata").toLocalDate(),
                        rs.getInt("progetto_id"),
                        rs.getString("tipo_attivita")
                );
                attivita.add(att);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero attività per coltivatore: " + e.getMessage());
        }
        return attivita;
    }


    public List<Attivita> getAttivitaByProgetto(int progettoId) {
        List<Attivita> attivita = new ArrayList<>();
        String sql = "SELECT * FROM attivita WHERE progetto_id = ? ORDER BY data_pianificata";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, progettoId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Attivita att = new Attivita(
                        rs.getInt("attivita_id"),
                        rs.getString("stato"),
                        rs.getInt("coltivatore_id"),
                        rs.getDate("data_pianificata").toLocalDate(),
                        rs.getInt("progetto_id"),
                        rs.getString("tipo_attivita")
                );
                attivita.add(att);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero attività per progetto: " + e.getMessage());
        }
        return attivita;
    }


    public Attivita getAttivitaById(int attivitaId) {
        String sql = "SELECT * FROM attivita WHERE attivita_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, attivitaId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Attivita(
                        rs.getInt("attivita_id"),
                        rs.getString("stato"),
                        rs.getInt("coltivatore_id"),
                        rs.getDate("data_pianificata").toLocalDate(),
                        rs.getInt("progetto_id"),
                        rs.getString("tipo_attivita")
                );
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero attività per ID: " + e.getMessage());
        }
        return null;
    }


    public List<Attivita> getAttivitaPerStato(String stato) {
        List<Attivita> attivita = new ArrayList<>();
        String sql = "SELECT * FROM attivita WHERE stato = ? ORDER BY data_pianificata";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, stato);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Attivita att = new Attivita(
                        rs.getInt("attivita_id"),
                        rs.getString("stato"),
                        rs.getInt("coltivatore_id"),
                        rs.getDate("data_pianificata").toLocalDate(),
                        rs.getInt("progetto_id"),
                        rs.getString("tipo_attivita")
                );
                attivita.add(att);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero attività per stato: " + e.getMessage());
        }
        return attivita;
    }


    public List<Attivita> getAttivitaPerData(LocalDate dataInizio, LocalDate dataFine) {
        List<Attivita> attivita = new ArrayList<>();
        String sql = "SELECT * FROM attivita WHERE data_pianificata BETWEEN ? AND ? ORDER BY data_pianificata";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(dataInizio));
            pstmt.setDate(2, Date.valueOf(dataFine));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Attivita att = new Attivita(
                        rs.getInt("attivita_id"),
                        rs.getString("stato"),
                        rs.getInt("coltivatore_id"),
                        rs.getDate("data_pianificata").toLocalDate(),
                        rs.getInt("progetto_id"),
                        rs.getString("tipo_attivita")
                );
                attivita.add(att);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero attività per periodo: " + e.getMessage());
        }
        return attivita;
    }


    public List<Attivita> getAttivitaOggi() {
        LocalDate oggi = LocalDate.now();
        return getAttivitaPerData(oggi, oggi);
    }


    public List<Attivita> getAttivitaSettimana() {
        LocalDate oggi = LocalDate.now();
        LocalDate fineSettimana = oggi.plusDays(7);
        return getAttivitaPerData(oggi, fineSettimana);
    }


    public boolean aggiornaAttivita(Attivita attivita) {
        String sql = "UPDATE attivita SET tipo_attivita = ?, data_pianificata = ?, stato = ? WHERE attivita_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, attivita.getTipoAttivita());
            pstmt.setDate(2, Date.valueOf(attivita.getDataPianificata()));
            pstmt.setString(3, attivita.getStato());
            pstmt.setInt(4, attivita.getAttivitaId());

            int righeAggiornate = pstmt.executeUpdate();
            return righeAggiornate > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'aggiornamento attività: " + e.getMessage());
            return false;
        }
    }


    public boolean aggiornaStatoAttivita(int attivitaId, String nuovoStato) {
        String sql = "UPDATE attivita SET stato = ? WHERE attivita_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nuovoStato);
            pstmt.setInt(2, attivitaId);

            int righeAggiornate = pstmt.executeUpdate();
            return righeAggiornate > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'aggiornamento stato attività: " + e.getMessage());
            return false;
        }
    }


    public boolean eliminaAttivita(int attivitaId) {
        String sql = "DELETE FROM attivita WHERE attivita_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, attivitaId);

            int righeCancellate = pstmt.executeUpdate();
            return righeCancellate > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'eliminazione attività: " + e.getMessage());
            return false;
        }
    }


    public List<String> getAttivitaComplete(int coltivatoreId) {
        List<String> attivita = new ArrayList<>();
        String sql = "SELECT a.*, ps.nome_progetto, l.nome_lotto, u.nome as nome_coltivatore " +
                "FROM attivita a " +
                "JOIN progetto_stagionale ps ON a.progetto_id = ps.progetto_id " +
                "JOIN lotto l ON ps.lotto_id = l.lotto_id " +
                "JOIN utente u ON a.coltivatore_id = u.user_id " +
                "WHERE a.coltivatore_id = ? " +
                "ORDER BY a.data_pianificata";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, coltivatoreId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String infoAttivita = String.format(
                        "Attività: %s | Progetto: %s | Lotto: %s | Data: %s | Stato: %s",
                        rs.getString("tipo_attivita"),
                        rs.getString("nome_progetto"),
                        rs.getString("nome_lotto"),
                        rs.getDate("data_pianificata").toString(),
                        rs.getString("stato")
                );
                attivita.add(infoAttivita);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero attività complete: " + e.getMessage());
        }
        return attivita;
    }


    public int contaAttivitaPerStato(String stato, int coltivatoreId) {
        String sql = "SELECT COUNT(*) FROM attivita WHERE stato = ? AND coltivatore_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, stato);
            pstmt.setInt(2, coltivatoreId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel conteggio attività: " + e.getMessage());
        }
        return 0;
    }


    public List<Attivita> getAttivitaInRitardo(int coltivatoreId) {
        List<Attivita> attivita = new ArrayList<>();
        String sql = "SELECT * FROM attivita WHERE coltivatore_id = ? AND data_pianificata < CURRENT_DATE AND stato != 'completata' ORDER BY data_pianificata";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, coltivatoreId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Attivita att = new Attivita(
                        rs.getInt("attivita_id"),
                        rs.getString("stato"),
                        rs.getInt("coltivatore_id"),
                        rs.getDate("data_pianificata").toLocalDate(),
                        rs.getInt("progetto_id"),
                        rs.getString("tipo_attivita")
                );
                attivita.add(att);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero attività in ritardo: " + e.getMessage());
        }
        return attivita;
    }


    public String getStatisticheAttivita(int coltivatoreId) {
        String sql = "SELECT " +
                "COUNT(*) as totale, " +
                "SUM(CASE WHEN stato = 'completata' THEN 1 ELSE 0 END) as completate, " +
                "SUM(CASE WHEN stato = 'in_corso' THEN 1 ELSE 0 END) as in_corso, " +
                "SUM(CASE WHEN stato = 'pianificata' THEN 1 ELSE 0 END) as pianificate " +
                "FROM attivita WHERE coltivatore_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, coltivatoreId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int totale = rs.getInt("totale");
                int completate = rs.getInt("completate");
                int inCorso = rs.getInt("in_corso");
                int pianificate = rs.getInt("pianificate");

                return String.format("Totale: %d | Completate: %d | In corso: %d | Pianificate: %d",
                        totale, completate, inCorso, pianificate);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero statistiche attività: " + e.getMessage());
        }
        return "Nessuna statistica disponibile";
    }
}