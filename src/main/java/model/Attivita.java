package model;
import java.util.Objects;
import java.time.LocalDate;

public class Attivita {
    private int attivitaId;
    private String stato; //'pianificato', 'in corso', 'completato'
    private int coltivatoreId;
    private LocalDate dataPianificata;
    private int progettoId;
    private String tipoAttivita;

    public Attivita() {
    }
    public Attivita(int attivitaId, String stato, int coltivatoreId, LocalDate dataPianificata, int progettoId, String tipoAttivita) {
        this.attivitaId = attivitaId;
        this.stato = stato;
        this.coltivatoreId = coltivatoreId;
        this.dataPianificata = dataPianificata;
        this.progettoId = progettoId;
        this.tipoAttivita = tipoAttivita;
    }

    public int getAttivitaId() {
        return attivitaId;
    }
    public void setAttivitaId(int attivitaId) {
        this.attivitaId = attivitaId;
    }

    public String getStato() {
        return stato;
    }
    public void setStato(String stato) {
        this.stato = stato;
    }

    public int getColtivatoreId() {
        return coltivatoreId;
    }
    public void setColtivatoreId(int coltivatoreId) {
        this.coltivatoreId = coltivatoreId;
    }

    public LocalDate getDataPianificata() {
        return dataPianificata;
    }
    public void setDataPianificata(LocalDate dataPianificata) {
        this.dataPianificata = dataPianificata;
    }

    public int getProgettoId() {
        return progettoId;
    }
    public void setProgettoId(int progettoId) {
        this.progettoId = progettoId;
    }

    public String getTipoAttivita() {
        return tipoAttivita;
    }
    public void setTipoAttivita(String tipoAttivita) {
        this.tipoAttivita = tipoAttivita;
    }

    @Override
    public String toString() {
        return attivitaId + " " + stato + " " + coltivatoreId + " " + dataPianificata + " " + progettoId + " " + tipoAttivita;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attivita attivita = (Attivita) o;
        return attivitaId == attivita.attivitaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(attivitaId);
    }

}
