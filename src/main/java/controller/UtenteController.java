package controller;

import dao.UtenteDAO;
import model.Utente;

import java.util.List;

public class UtenteController {
    private UtenteDAO utenteDAO;

    public UtenteController() {
        this.utenteDAO = new UtenteDAO();
    }

    public boolean registraUtente(Utente utente) {
        return utenteDAO.inserisciUtente(utente);
    }

    public Utente login(String username, String password) {
        return utenteDAO.autenticaUtente(username, password);
    }

    public List<Utente> getListaUtenti() {
        return utenteDAO.listaUtenti();
    }

    public boolean aggiornaUtente(Utente utente) {
        return utenteDAO.aggiornaUtente(utente);
    }

    public boolean eliminaUtente(Utente utente) {
        return utenteDAO.rimuoviUtente(utente);
    }

    public boolean esisteUsername(String username) {
        return utenteDAO.esisteUsername(username);
    }

    public Utente getUtenteById(int userId) {
        return utenteDAO.getUtenteById(userId);
    }

    public Utente loginUtente(String username, String password) {
        return utenteDAO.autenticaUtente(username, password);
    }

    public List<Utente> listaUtenti() {
        return utenteDAO.listaUtenti();
    }
}
