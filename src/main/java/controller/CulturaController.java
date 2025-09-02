package controller;

import dao.CulturaDAO;
import model.Cultura;
import java.util.List;

public class CulturaController {
    private CulturaDAO culturaDAO;

    public CulturaController() {
        this.culturaDAO = new CulturaDAO();
    }

    public boolean creaCultura(Cultura cultura) {
        return culturaDAO.inserisciCultura(cultura);
    }

    public List<Cultura> getListaCultura() {
        return culturaDAO.getCulturePerProgetto(1);
    }

    public List<Cultura> getAllCultura() {
        return culturaDAO.getAllCulture();
    }

    public boolean aggiornaCultura(Cultura cultura) {
        return culturaDAO.aggiornaCultura(cultura);
    }

    public boolean eliminaCultura(int culturaId) {
        return culturaDAO.eliminaCultura(culturaId);
    }

    public boolean inserisciCultura(Cultura nuovaCultura) {
        return culturaDAO.inserisciCultura(nuovaCultura);
    }
}
