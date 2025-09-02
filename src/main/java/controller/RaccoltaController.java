package controller;

import dao.RaccoltaDAO;
import model.Raccolta;
import java.util.List;
import java.time.LocalDate;

public class RaccoltaController {
    private RaccoltaDAO raccoltaDAO;

    public RaccoltaController() {
        this.raccoltaDAO = new RaccoltaDAO();
    }

    public boolean creaRaccolta(Raccolta raccolta) {
        return raccoltaDAO.inserisciRaccolta(raccolta);
    }

    public List<Raccolta> getListaRaccolte() {
        return raccoltaDAO.getRaccoltePerPeriodo(LocalDate.now().minusMonths(1), LocalDate.now());
    }

    public boolean aggiornaRaccolta(Raccolta raccolta) {
        return raccoltaDAO.aggiornaRaccolta(raccolta);
    }

    public boolean eliminaRaccolta(int raccoltaId) {
        return raccoltaDAO.eliminaRaccolta(raccoltaId);
    }
}
