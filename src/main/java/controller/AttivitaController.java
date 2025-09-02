package controller;

import dao.AttivitaDAO;
import model.Attivita;
import java.util.List;

public class AttivitaController {
    private AttivitaDAO attivitaDAO;

    public AttivitaController() {
        this.attivitaDAO = new AttivitaDAO();
    }

    public boolean creaAttivita(Attivita attivita) {
        return attivitaDAO.inserisciAttivita(attivita);
    }

    public List<Attivita> getListaAttivita() {
        return attivitaDAO.getAttivitaByProgetto(1);
    }

    public List<Attivita> getAttivitaByColtivatore(int userId) {
        return attivitaDAO.getAttivitaByColtivatore(userId);
    }

    public boolean aggiornaAttivita(Attivita attivita) {
        return attivitaDAO.aggiornaAttivita(attivita);
    }

    public boolean eliminaAttivita(int attivitaId) {
        return attivitaDAO.eliminaAttivita(attivitaId);
    }

    public String getStatisticheAttivita(int userId) {
        return attivitaDAO.getStatisticheAttivita(userId);
    }

    public boolean inserisciAttivita(Attivita nuovaAttivita) {
        return attivitaDAO.inserisciAttivita(nuovaAttivita);
    }

    public boolean aggiornaStatoAttivita(int attivitaId, String nuovoStato) {
        return attivitaDAO.aggiornaStatoAttivita(attivitaId, nuovoStato);
    }

    public List<Attivita> getAttivitaByProgetto(int progettoId) {
        return attivitaDAO.getAttivitaByProgetto(progettoId);
    }
}
