package model;
import java.util.Objects;
import java.time.LocalDate;

public class Raccolta {
    private int raccoltaId;
    private int quantita;
    private LocalDate dataRaccolta;
    private int attivitaId;

    public Raccolta() {
    }
    public Raccolta(int raccoltaId, int quantita, LocalDate dataRaccolta, int attivitaId) {
        this.raccoltaId = raccoltaId;
        this.quantita = quantita;
        this.dataRaccolta = dataRaccolta;
        this.attivitaId = attivitaId;
    }

    public int getRaccoltaId() {
        return raccoltaId;
    }
    public void setRaccoltaId(int raccoltaId) {
        this.raccoltaId = raccoltaId;
    }

    public int getQuantita() {
        return quantita;
    }
    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public LocalDate getDataRaccolta() {
        return dataRaccolta;
    }
    public void setDataRaccolta(LocalDate dataRaccolta) {
        this.dataRaccolta = dataRaccolta;
    }

    public int getAttivitaId() {
        return attivitaId;
    }
    public void setAttivitaId(int attivitaId) {
        this.attivitaId = attivitaId;
    }

    @Override
    public String toString() {
        return quantita + " " + dataRaccolta + " " + attivitaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Raccolta raccolta = (Raccolta) o;
        return raccoltaId == raccolta.raccoltaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(raccoltaId);
    }


}
