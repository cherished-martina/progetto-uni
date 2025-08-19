package database;

import model.Utente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//DAO sta per Data Access Object
public class UtenteDAO {
    private Connection connection;

    public UtenteDAO() {
        DatabaseConnection dbConn = new DatabaseConnection();
        this.connection = dbConn.getConnection();
    }


    public boolean inserisciUtente(Utente utente) {
        String sql = "INSERT INTO utente (nome, cognome, email, password, tipo_utente, username) VALUES (?,?,?,?,?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, utente.getNome());
            pstmt.setString(2, utente.getCognome());
            pstmt.setString(3, utente.getEmail());
            pstmt.setString(4, utente.getPassword());
            pstmt.setString(5, utente.getTipoUtente());
            pstmt.setString(6, utente.getUsername());

            int righeInserite = pstmt.executeUpdate();
            return righeInserite > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'inserimento utente" + e.getMessage());
            return false;
        }
    }


    public Utente autenticaUtente(String username, String password) {
        String sql = "SELECT * FROM utente WHERE username=? AND password=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Utente utente = new Utente(
                        rs.getString( "nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("tipo_utente"),
                        rs.getString("username")
                );
                utente.setUserId(rs.getInt("user_id"));
                return utente;


            }
        } catch (SQLException e) {
            System.err.println("Errore nell'autenticazione utente" + e.getMessage());
        }
        return null; //autenticazione fallita
    }


    public List<Utente> listaUtenti() {
        List<Utente> utenti = new ArrayList<>();
        String sql = "SELECT user_id, nome, cognome, email, tipo_utente, username FROM utente";

        System.out.println("=== DEBUG UtenteDAO.listaUtenti() ===");
        System.out.println("Eseguendo query: " + sql);


        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int contatore = 0;
            while (rs.next()) {
                contatore++;
                System.out.println("Riga " + contatore + ":");
                System.out.println("  user_id: " + rs.getInt("user_id"));
                System.out.println("  nome: " + rs.getString("nome"));
                System.out.println("  cognome: " + rs.getString("cognome"));
                System.out.println("  email: " + rs.getString("email"));
                System.out.println("  tipo_utente: " + rs.getString("tipo_utente"));
                System.out.println("  username: " + rs.getString("username"));

                // Creiamo utente senza password (non serve per la lista)
                Utente utente = new Utente(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        "", // password vuota - tanto non serve per listare
                        rs.getString("tipo_utente"),
                        rs.getString("username")
                );
                utente.setUserId(rs.getInt("user_id"));
                utenti.add(utente);
                System.out.println("  → Utente aggiunto alla lista");
            }
            System.out.println("Totale utenti processati: " + contatore);
            System.out.println("Dimensione lista finale: " + utenti.size());

        } catch (SQLException e) {
            System.err.println("ERRORE SQL in listaUtenti: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== FINE DEBUG UtenteDAO ===");
        return utenti;
    }


    public boolean aggiornaUtente(Utente utente) {
        String sql = "UPDATE utente SET password = ?, nome = ?, cognome = ?, email = ?, tipoUtente = ? WHERE username=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, utente.getPassword());
            pstmt.setString(2, utente.getNome());
            pstmt.setString(3, utente.getCognome());
            pstmt.setString(4, utente.getEmail());
            pstmt.setString(5, utente.getTipoUtente());
            pstmt.setString(6, utente.getUsername());

            int righeAggiornate = pstmt.executeUpdate();
            return righeAggiornate > 0;
        } catch (SQLException e) {
            System.err.println("Errore nell'aggiornamento utente" + e.getMessage());
            return false;
        }
    }


    public boolean rimuoviUtente(Utente utente) {
        String sql = "DELETE FROM utente WHERE username=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, utente.getUsername());

            int righeCancellate = pstmt.executeUpdate();
            return righeCancellate > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'aggiornamento utente" + e.getMessage());
            return false;
        }
    }


    public boolean esisteUsername(String username) {
        String sql = "SELECT * FROM utente WHERE username=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Errore nella verifica utente" + e.getMessage());
        }
        return false;
    }



    public Utente getUtenteById(int userId) {
        String sql = "SELECT * FROM utente WHERE user_id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Utente utente = new Utente(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("tipo_utente"),
                        rs.getString("username")
                );
                utente.setUserId(rs.getInt("user_id"));
                return utente;
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero utente per ID: " + e.getMessage());
        }
        return null;


    }

}

