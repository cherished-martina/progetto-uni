package controller;

import dao.LottoDAO;
import model.Lotto;
import java.util.List;

public class LottoController {
    private LottoDAO lottoDAO;

    public LottoController() {
        this.lottoDAO = new LottoDAO();
    }

    public boolean creaLotto(Lotto lotto) {
        return lottoDAO.inserisciLotto(lotto);
    }

    public List<Lotto> getListaLotti() {
        return lottoDAO.getAllLotti();
    }

    public List<Lotto> getLottiByProprietario(int userId) {
        return lottoDAO.getLottiByProprietario(userId);
    }

    public boolean aggiornaLotto(Lotto lotto) {
        return lottoDAO.aggiornaLotto(lotto);
    }

    public boolean eliminaLotto(int lottoId) {
        return lottoDAO.eliminaLotto(lottoId);
    }

    public boolean inserisciLotto(Lotto nuovoLotto) {
        return lottoDAO.inserisciLotto(nuovoLotto);
    }
}
