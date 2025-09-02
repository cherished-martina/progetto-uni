package dao;

import model.Cultura;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CulturaDAO {
    private Connection connection;

    public CulturaDAO() {
        DatabaseConnection dbConn = new DatabaseConnection();
        this.connection = dbConn.getConnection();
    }


    public boolean inserisciCultura(Cultura cultura) {
        String sql = "INSERT INTO cultura (nome_cultura, descrizione, tempo_maturazione) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cultura.getNomeCultura());
            pstmt.setString(2, cultura.getDescrizione());
            pstmt.setInt(3, cultura.getTempoMaturazione());

            int righeInserite = pstmt.executeUpdate();
            return righeInserite > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'inserimento cultura: " + e.getMessage());
            return false;
        }
    }


    public List<Cultura> getAllCulture() {
        List<Cultura> culture = new ArrayList<>();
        String sql = "SELECT * FROM cultura ORDER BY nome_cultura";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cultura cultura = new Cultura(
                        rs.getInt("cultura_id"),
                        rs.getString("nome_cultura"),
                        rs.getString("descrizione"),
                        rs.getInt("tempo_maturazione")
                );
                culture.add(cultura);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero culture: " + e.getMessage());
        }
        return culture;
    }


    public Cultura getCulturaById(int culturaId) {
        String sql = "SELECT * FROM cultura WHERE cultura_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, culturaId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Cultura(
                        rs.getInt("cultura_id"),
                        rs.getString("nome_cultura"),
                        rs.getString("descrizione"),
                        rs.getInt("tempo_maturazione")
                );
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero cultura per ID: " + e.getMessage());
        }
        return null;
    }


    public List<Cultura> cercaCulturePerNome(String nomeRicerca) {
        List<Cultura> culture = new ArrayList<>();
        String sql = "SELECT * FROM cultura WHERE nome_cultura ILIKE ? ORDER BY nome_cultura";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nomeRicerca + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Cultura cultura = new Cultura(
                        rs.getInt("cultura_id"),
                        rs.getString("nome_cultura"),
                        rs.getString("descrizione"),
                        rs.getInt("tempo_maturazione")
                );
                culture.add(cultura);
            }
        } catch (SQLException e) {
            System.err.println("Errore nella ricerca culture: " + e.getMessage());
        }
        return culture;
    }


    public boolean aggiornaCultura(Cultura cultura) {
        String sql = "UPDATE cultura SET nome_cultura = ?, descrizione = ?, tempo_maturazione = ? WHERE cultura_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cultura.getNomeCultura());
            pstmt.setString(2, cultura.getDescrizione());
            pstmt.setInt(3, cultura.getTempoMaturazione());
            pstmt.setInt(4, cultura.getCulturaId());

            int righeAggiornate = pstmt.executeUpdate();
            return righeAggiornate > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'aggiornamento cultura: " + e.getMessage());
            return false;
        }
    }


    public boolean eliminaCultura(int culturaId) {
        String sql = "DELETE FROM cultura WHERE cultura_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, culturaId);

            int righeCancellate = pstmt.executeUpdate();
            return righeCancellate > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'eliminazione cultura: " + e.getMessage());
            return false;
        }
    }


    public boolean esisteCultura(String nomeCultura) {
        String sql = "SELECT COUNT(*) FROM cultura WHERE nome_cultura ILIKE ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nomeCultura);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Errore nella verifica esistenza cultura: " + e.getMessage());
        }
        return false;
    }


    public List<Cultura> getCulturePerProgetto(int progettoId) {
        List<Cultura> culture = new ArrayList<>();
        String sql = "SELECT c.* FROM cultura c " +
                "JOIN progetto_cultura pc ON c.cultura_id = pc.cultura_id " +
                "WHERE pc.progetto_id = ? " +
                "ORDER BY c.nome_cultura";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, progettoId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Cultura cultura = new Cultura(
                        rs.getInt("cultura_id"),
                        rs.getString("nome_cultura"),
                        rs.getString("descrizione"),
                        rs.getInt("tempo_maturazione")
                );
                culture.add(cultura);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero culture per progetto: " + e.getMessage());
        }
        return culture;
    }
}
