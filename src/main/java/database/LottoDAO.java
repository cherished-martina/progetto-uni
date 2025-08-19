package database;

import model.Lotto;
import model.Utente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LottoDAO {
    private Connection connection;
    private UtenteDAO utenteDAO;

    public LottoDAO() {
        DatabaseConnection dbConn = new DatabaseConnection();
        this.connection = dbConn.getConnection();
        this.utenteDAO = new UtenteDAO(); // Per recuperare i proprietari
    }


    public boolean inserisciLotto(Lotto lotto) {
        String sql = "INSERT INTO lotto (nome_lotto, ubicazione, dimensione, proprietario_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, lotto.getNomeLotto());
            pstmt.setString(2, lotto.getUbicazione());
            pstmt.setDouble(3, lotto.getDimensione());
            pstmt.setInt(4, lotto.getProprietario()); // Questo chiama getUserId() del proprietario

            int righeInserite = pstmt.executeUpdate();
            return righeInserite > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'inserimento lotto: " + e.getMessage());
            return false;
        }
    }


    public List<Lotto> getLottiByProprietario(int proprietarioId) {
        List<Lotto> lotti = new ArrayList<>();
        String sql = "SELECT * FROM lotto WHERE proprietario_id = ? ORDER BY nome_lotto";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, proprietarioId);
            ResultSet rs = pstmt.executeQuery();

            // recupera i dati del proprietario
            Utente proprietario = utenteDAO.getUtenteById(proprietarioId);

            while (rs.next()) {
                Lotto lotto = new Lotto(
                        rs.getInt("lotto_id"),
                        rs.getString("nome_lotto"),
                        rs.getString("ubicazione"),
                        rs.getDouble("dimensione"),
                        proprietario
                );
                lotti.add(lotto);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero lotti per proprietario: " + e.getMessage());
        }
        return lotti;
    }

    // ottenere tutti i lotti per gli amministratori
    public List<Lotto> getAllLotti() {
        List<Lotto> lotti = new ArrayList<>();
        String sql = "SELECT l.*, u.user_id, u.username, u.nome, u.cognome, u.email, u.password, u.tipo_utente " +
                "FROM lotto l JOIN utente u ON l.proprietario_id = u.user_id " +
                "ORDER BY l.nome_lotto";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Utente proprietario = new Utente(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("tipo_utente"),
                        rs.getString("username")
                );
                proprietario.setUserId(rs.getInt("user_id"));


                Lotto lotto = new Lotto(
                        rs.getInt("lotto_id"),
                        rs.getString("nome_lotto"),
                        rs.getString("ubicazione"),
                        rs.getDouble("dimensione"),
                        proprietario
                );
                lotti.add(lotto);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero di tutti i lotti: " + e.getMessage());
        }
        return lotti;
    }


    public Lotto getLottoById(int lottoId) {
        String sql = "SELECT l.*, u.user_id, u.username, u.nome, u.cognome, u.email, u.password, u.tipo_utente " +
                "FROM lotto l JOIN utente u ON l.proprietario_id = u.user_id " +
                "WHERE l.lotto_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, lottoId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                Utente proprietario = new Utente(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("tipo_utente"),
                        rs.getString("username")
                );
                proprietario.setUserId(rs.getInt("user_id"));


                return new Lotto(
                        rs.getInt("lotto_id"),
                        rs.getString("nome_lotto"),
                        rs.getString("ubicazione"),
                        rs.getDouble("dimensione"),
                        proprietario
                );
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero lotto per ID: " + e.getMessage());
        }
        return null;
    }


    public List<Lotto> cercaLotti(String termine) {
        List<Lotto> lotti = new ArrayList<>();
        String sql = "SELECT l.*, u.user_id, u.username, u.nome, u.cognome, u.email, u.password, u.tipo_utente " +
                "FROM lotto l JOIN utente u ON l.proprietario_id = u.user_id " +
                "WHERE l.nome_lotto ILIKE ? OR l.ubicazione ILIKE ? " +
                "ORDER BY l.nome_lotto";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String searchTerm = "%" + termine + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                Utente proprietario = new Utente(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("tipo_utente"),
                        rs.getString("username")
                );
                proprietario.setUserId(rs.getInt("user_id"));


                Lotto lotto = new Lotto(
                        rs.getInt("lotto_id"),
                        rs.getString("nome_lotto"),
                        rs.getString("ubicazione"),
                        rs.getDouble("dimensione"),
                        proprietario
                );
                lotti.add(lotto);
            }
        } catch (SQLException e) {
            System.err.println("Errore nella ricerca lotti: " + e.getMessage());
        }
        return lotti;
    }


    public boolean aggiornLotto(Lotto lotto) {
        String sql = "UPDATE lotto SET nome_lotto = ?, ubicazione = ?, dimensione = ? WHERE lotto_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, lotto.getNomeLotto());
            pstmt.setString(2, lotto.getUbicazione());
            pstmt.setDouble(3, lotto.getDimensione());
            pstmt.setInt(4, lotto.getLottoId());

            int righeAggiornate = pstmt.executeUpdate();
            return righeAggiornate > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'aggiornamento lotto: " + e.getMessage());
            return false;
        }
    }


    public boolean eliminaLotto(int lottoId) {
        String sql = "DELETE FROM lotto WHERE lotto_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, lottoId);

            int righeCancellate = pstmt.executeUpdate();
            return righeCancellate > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'eliminazione lotto: " + e.getMessage());
            return false;
        }
    }


    public String getStatisticheLotti(int proprietarioId) {
        String sql = "SELECT COUNT(*) as numero_lotti, SUM(dimensione) as superficie_totale, AVG(dimensione) as dimensione_media " +
                "FROM lotto WHERE proprietario_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, proprietarioId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int numeroLotti = rs.getInt("numero_lotti");
                double superficieTotale = rs.getDouble("superficie_totale");
                double dimensioneMedia = rs.getDouble("dimensione_media");

                return String.format("Lotti: %d | Superficie totale: %.2f m² | Dimensione media: %.2f m²",
                        numeroLotti, superficieTotale, dimensioneMedia);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero statistiche lotti: " + e.getMessage());
        }
        return "Nessuna statistica disponibile";
    }


    public boolean esisteLotto(String nomeLotto, int proprietarioId) {
        String sql = "SELECT COUNT(*) FROM lotto WHERE nome_lotto ILIKE ? AND proprietario_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nomeLotto);
            pstmt.setInt(2, proprietarioId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Errore nella verifica esistenza lotto: " + e.getMessage());
        }
        return false;
    }
}
