package model;
import java.util.Objects;

public class Lotto {
    private int lottoId;
    private String nomeLotto;
    private String ubicazione;
    private double dimensione;
    private Utente proprietario; //riferimento ad utente

    public Lotto() {}
    public Lotto (int lottoId, String nomeLotto, String ubicazione, double dimensione, Utente proprietario ) {
        this.lottoId = lottoId;
        this.nomeLotto = nomeLotto;
        this.ubicazione = ubicazione;
        this.dimensione = dimensione;
        this.proprietario = proprietario;
    }

    public int getLottoId() {
        return lottoId;
    }
    public void setLottoId(int lottoId) {
        this.lottoId = lottoId;
    }

    public String getNomeLotto() {
        return nomeLotto;
    }
    public void setNomeLotto(String nomeLotto) {
        this.nomeLotto = nomeLotto;
    }

    public String getUbicazione() {
        return ubicazione;
    }
    public void setUbicazione(String ubicazione) {
        this.ubicazione = ubicazione;
    }

    public double getDimensione() {
        return dimensione;
    }
    public void setDimensione(double dimensione) {
        this.dimensione = dimensione;
    }


    public int getProprietario() {
        return proprietario.getUserId();
    }
    public void setProprietario(Utente proprietario) {
        this.proprietario = proprietario;
    }

    @Override
    public String toString() {
        return nomeLotto + " " + ubicazione + " " + dimensione + " " + proprietario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lotto lotto = (Lotto) o;
        return lottoId == lotto.lottoId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lottoId);
    }


}
