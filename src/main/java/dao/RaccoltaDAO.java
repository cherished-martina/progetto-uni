package dao;

import model.Raccolta;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RaccoltaDAO {
    private Connection connection;

    public RaccoltaDAO() {
        DatabaseConnection dbConn = new DatabaseConnection();
        this.connection = dbConn.getConnection();
    }

    public boolean inserisciRaccolta(Raccolta raccolta) {
        String sql = "INSERT INTO raccolta (quantita, data_raccolta, attivita_id) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, raccolta.getQuantita()); // Cambiato da setInt a setDouble per essere sicuri
            pstmt.setDate(2, Date.valueOf(raccolta.getDataRaccolta()));
            pstmt.setInt(3, raccolta.getAttivitaId());

            int righeInserite = pstmt.executeUpdate();
            return righeInserite > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'inserimento raccolta: " + e.getMessage());
            return false;
        }
    }

    public List<Raccolta> getRaccolteByAttivita(int attivitaId) {
        List<Raccolta> raccolte = new ArrayList<>();
        String sql = "SELECT * FROM raccolta WHERE attivita_id = ? ORDER BY data_raccolta DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, attivitaId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Raccolta raccolta = new Raccolta(
                        rs.getInt("raccolta_id"),
                        rs.getInt("quantita"),
                        rs.getDate("data_raccolta").toLocalDate(),
                        rs.getInt("attivita_id")
                );
                raccolte.add(raccolta);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero raccolte per attività: " + e.getMessage());
        }
        return raccolte;
    }


    public Raccolta getRaccoltaById(int raccoltaId) {
        String sql = "SELECT * FROM raccolta WHERE raccolta_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, raccoltaId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Raccolta(
                        rs.getInt("raccolta_id"),
                        rs.getInt("quantita"),
                        rs.getDate("data_raccolta").toLocalDate(),
                        rs.getInt("attivita_id")
                );
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero raccolta per ID: " + e.getMessage());
        }
        return null;
    }


    public List<Raccolta> getRaccoltePerPeriodo(LocalDate dataInizio, LocalDate dataFine) {
        List<Raccolta> raccolte = new ArrayList<>();
        String sql = "SELECT * FROM raccolta WHERE data_raccolta BETWEEN ? AND ? ORDER BY data_raccolta DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(dataInizio));
            pstmt.setDate(2, Date.valueOf(dataFine));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Raccolta raccolta = new Raccolta(
                        rs.getInt("raccolta_id"),
                        rs.getInt("quantita"),
                        rs.getDate("data_raccolta").toLocalDate(),
                        rs.getInt("attivita_id")
                );
                raccolte.add(raccolta);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero raccolte per periodo: " + e.getMessage());
        }
        return raccolte;
    }


    public boolean aggiornaRaccolta(Raccolta raccolta) {
        String sql = "UPDATE raccolta SET quantita = ?, data_raccolta = ? WHERE raccolta_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, raccolta.getQuantita());
            pstmt.setDate(2, Date.valueOf(raccolta.getDataRaccolta()));
            pstmt.setInt(3, raccolta.getRaccoltaId());

            int righeAggiornate = pstmt.executeUpdate();
            return righeAggiornate > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'aggiornamento raccolta: " + e.getMessage());
            return false;
        }
    }


    public boolean eliminaRaccolta(int raccoltaId) {
        String sql = "DELETE FROM raccolta WHERE raccolta_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, raccoltaId);

            int righeCancellate = pstmt.executeUpdate();
            return righeCancellate > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'eliminazione raccolta: " + e.getMessage());
            return false;
        }
    }


    public List<String> getReportRaccoltePerLotto(int proprietarioId) {
        List<String> report = new ArrayList<>();
        String sql = "SELECT l.nome_lotto, " +
                "COUNT(r.raccolta_id) as numero_raccolte, " +
                "SUM(r.quantita) as quantita_totale " +
                "FROM lotto l " +
                "JOIN progetto_stagionale ps ON l.lotto_id = ps.lotto_id " +
                "JOIN attivita a ON ps.progetto_id = a.progetto_id " +
                "JOIN raccolta r ON a.attivita_id = r.attivita_id " +
                "WHERE l.proprietario_id = ? " +
                "GROUP BY l.nome_lotto " +
                "ORDER BY quantita_totale DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, proprietarioId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String infoLotto = String.format("Lotto: %s | Raccolte: %d | Totale: %d kg",
                        rs.getString("nome_lotto"),
                        rs.getInt("numero_raccolte"),
                        rs.getInt("quantita_totale"));
                report.add(infoLotto);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel report raccolte per lotto: " + e.getMessage());
        }
        return report;
    }


    public List<String> getRaccolteComplete(int proprietarioId) {
        List<String> raccolte = new ArrayList<>();
        String sql = "SELECT r.*, l.nome_lotto, ps.nome_progetto, a.tipo_attivita " +
                "FROM raccolta r " +
                "JOIN attivita a ON r.attivita_id = a.attivita_id " +
                "JOIN progetto_stagionale ps ON a.progetto_id = ps.progetto_id " +
                "JOIN lotto l ON ps.lotto_id = l.lotto_id " +
                "WHERE l.proprietario_id = ? " +
                "ORDER BY r.data_raccolta DESC " +
                "LIMIT 50"; // Limitiamo per evitare troppi risultati

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, proprietarioId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String infoRaccolta = String.format(
                        "Data: %s | Lotto: %s | Quantità: %d kg | Progetto: %s | Attività: %s",
                        rs.getDate("data_raccolta").toString(),
                        rs.getString("nome_lotto"),
                        rs.getInt("quantita"),
                        rs.getString("nome_progetto"),
                        rs.getString("tipo_attivita")
                );
                raccolte.add(infoRaccolta);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero raccolte complete: " + e.getMessage());
        }
        return raccolte;
    }


    public String getStatisticheRaccolte(int proprietarioId) {
        String sql = "SELECT " +
                "COUNT(r.raccolta_id) as totale_raccolte, " +
                "SUM(r.quantita) as quantita_totale, " +
                "AVG(r.quantita) as quantita_media " +
                "FROM raccolta r " +
                "JOIN attivita a ON r.attivita_id = a.attivita_id " +
                "JOIN progetto_stagionale ps ON a.progetto_id = ps.progetto_id " +
                "JOIN lotto l ON ps.lotto_id = l.lotto_id " +
                "WHERE l.proprietario_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, proprietarioId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int totaleRaccolte = rs.getInt("totale_raccolte");
                int quantitaTotale = rs.getInt("quantita_totale");
                double quantitaMedia = rs.getDouble("quantita_media");

                return String.format("Raccolte totali: %d | Quantità totale: %d kg | Media per raccolta: %.1f kg",
                        totaleRaccolte, quantitaTotale, quantitaMedia);
            }
        } catch (SQLException e) {
            System.err.println("Errore nelle statistiche raccolte: " + e.getMessage());
        }
        return "Nessuna statistica disponibile";
    }


    public List<Raccolta> getRaccolteOggi() {
        LocalDate oggi = LocalDate.now();
        return getRaccoltePerPeriodo(oggi, oggi);
    }


    public List<Raccolta> getRaccolteSettimana() {
        LocalDate oggi = LocalDate.now();
        LocalDate settimanaPrima = oggi.minusDays(7);
        return getRaccoltePerPeriodo(settimanaPrima, oggi);
    }

    
    public int contaRaccolte(int proprietarioId) {
        String sql = "SELECT COUNT(r.raccolta_id) " +
                "FROM raccolta r " +
                "JOIN attivita a ON r.attivita_id = a.attivita_id " +
                "JOIN progetto_stagionale ps ON a.progetto_id = ps.progetto_id " +
                "JOIN lotto l ON ps.lotto_id = l.lotto_id " +
                "WHERE l.proprietario_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, proprietarioId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel conteggio raccolte: " + e.getMessage());
        }
        return 0;
    }
}