package controller;

import dao.ProgettoStagionaleDAO;
import model.ProgettoStagionale;
import java.util.List;

public class ProgettoStagionaleController {
    private ProgettoStagionaleDAO progettoStagionaleDAO;

    public ProgettoStagionaleController() {
        this.progettoStagionaleDAO = new ProgettoStagionaleDAO();
    }

    public boolean creaProgettoStagionale(ProgettoStagionale progetto) {
        return progettoStagionaleDAO.inserisciProgetto(progetto);
    }

    public ProgettoStagionale getProgettoById(int progettoId) {
        return progettoStagionaleDAO.getProgettoById(progettoId);
    }

    public boolean aggiornaProgettoStagionale(ProgettoStagionale progetto) {
        return progettoStagionaleDAO.aggiornaProgetto(progetto);
    }

    public List<ProgettoStagionale> getProgettiByProprietario(int userId) {
        return progettoStagionaleDAO.getProgettiByProprietario(userId);
    }

    public boolean eliminaProgettoStagionale(int progettoId) {
        return progettoStagionaleDAO.eliminaProgetto(progettoId);
    }

    public boolean inserisciProgetto(ProgettoStagionale nuovoProgetto) {
        return progettoStagionaleDAO.inserisciProgetto(nuovoProgetto);
    }
}
