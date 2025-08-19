package model;
import java.util.Objects;

public class Utente {
    private int userId;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String tipoUtente;
    private String username;

    public Utente() {
    }
    public Utente(String nome, String cognome, String email, String password, String tipoUtente, String username) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.tipoUtente = tipoUtente;
        this.username = username;
    }


    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipoUtente() {
        return tipoUtente;
    }
    public void setTipoUtente(String tipoUtente) {
        this.tipoUtente = tipoUtente;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return nome + " " + cognome + " " + email + " " + password + " " + tipoUtente + " " + username;
    }

    //metodo equals per controllare se un utente è già registrato, automaticamente dobbiamo definire anche hashCode (code univoco per ogni oggetto)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utente utente = (Utente) o;
        return Objects.equals(username, utente.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

}
