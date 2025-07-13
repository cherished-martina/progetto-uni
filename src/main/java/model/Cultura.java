package model;
import java.util.Objects;

public class Cultura {
    private int culturaId;
    private String nomeCultura;
    private String descrizione;
    private int tempoMaturazione; //tempo di maturazione in giorni

    public Cultura(){}
    public Cultura(int culturaId, String nomeCultura, String descrizione, int tempoMaturazione) {
        this.culturaId = culturaId;
        this.nomeCultura = nomeCultura;
        this.descrizione = descrizione;
        this.tempoMaturazione = tempoMaturazione;

    }
    public int getCulturaId() {
        return culturaId;
    }
    public void setCulturaId(int culturaId) {
        this.culturaId = culturaId;
    }

    public String getNomeCultura() {
        return nomeCultura;
    }
    public void setNomeCultura(String nomeCultura) {
        this.nomeCultura = nomeCultura;
    }
    public String getDescrizione() {
        return descrizione;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getTempoMaturazione() {
        return tempoMaturazione;
    }
    public void setTempoMaturazione(int tempoMaturazione) {
        this.tempoMaturazione = tempoMaturazione;
    }

    @Override
    public String toString() {
        return nomeCultura + "" + descrizione + "" + tempoMaturazione;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cultura cultura = (Cultura) o;
        return Objects.equals(nomeCultura, cultura.nomeCultura);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeCultura);
    }


}
