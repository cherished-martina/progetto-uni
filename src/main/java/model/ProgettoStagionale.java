package model;
import java.time.LocalDate;
import java.util.Objects;

public class ProgettoStagionale {
    private int progettoId;
    private String nomeProgetto;
    private int anno;
    private String stato; //'pianificato', 'in corso','completato'
    private String stagione; // primavera, estate, autunno, inverno
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private int proprietarioId;
    private int lottoId;

    public ProgettoStagionale() {
    }

    public ProgettoStagionale(int progettoId, String nomeProgetto, int anno, String stato, String stagione, LocalDate dataInizio, LocalDate dataFine, int proprietarioId, int lottoId) {
        this.progettoId = progettoId;
        this.nomeProgetto = nomeProgetto;
        this.anno = anno;
        this.stato = stato;
        this.stagione = stagione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.proprietarioId = proprietarioId;
        this.lottoId = lottoId;
    }

    public int getProgettoId() {
        return progettoId;
    }
    public void setProgettoId(int progettoId) {
        this.progettoId = progettoId;
    }

    public String getNomeProgetto() {
        return nomeProgetto;
    }
    public void setNomeProgetto(String nomeProgetto) {
        this.nomeProgetto = nomeProgetto;
    }
    public int getAnno() {
        return anno;
    }
    public void setAnno(int anno) {
        this.anno = anno;
    }

    public String getStato() {
        return stato;
    }
    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getStagione() {
        return stagione;
    }
    public void setStagione(String stagione) {
        this.stagione = stagione;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }
    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }
    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public int getProprietarioId() {
        return proprietarioId;
    }
    public void setProprietarioId(int proprietarioId) {
        this.proprietarioId = proprietarioId;
    }

    public int getLottoId() {
        return lottoId;
    }
    public void setLottoId(int lottoId) {
        this.lottoId = lottoId;
    }

    @Override
    public String toString() {
        return nomeProgetto + " " + anno + " " + stato + " " + stagione + " " + dataInizio + " " + dataFine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgettoStagionale that = (ProgettoStagionale) o;
        return progettoId == that.progettoId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(progettoId);
    }

}
