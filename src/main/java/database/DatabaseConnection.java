package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//connettiamo con il database PostgreSQL
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/uninabiogarden";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Criscuolo05";

    private Connection connection = null;

    //metodo per ottenere la connessione
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connection established");
            }
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    //metodo per chiudere la connessione
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }

    }


}
