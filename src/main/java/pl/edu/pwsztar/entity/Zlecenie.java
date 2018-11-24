package pl.edu.pwsztar.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "zlecenie")
public class Zlecenie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zlecenia", nullable = false)
    private int idZlecenia;

    @Column(name = "opis", length = 80)
    private String opis;

    @Column(name = "data_rozpoczecia")
    private Timestamp dataRozpoczecia;

    @Column(name = "data_zakonczenia")
    private Timestamp dataZakonczenia;

    @Column(name = "rabat", precision = 2)
    private BigDecimal rabat;

    @Column(name = "cena_koncowa", precision = 2)
    private BigDecimal cenaKoncowa;

    public int getIdZlecenia() {
        return idZlecenia;
    }

    public void setIdZlecenia(int idZlecenia) {
        this.idZlecenia = idZlecenia;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Timestamp getDataRozpoczecia() {
        return dataRozpoczecia;
    }

    public void setDataRozpoczecia(Timestamp dataRozpoczecia) {
        this.dataRozpoczecia = dataRozpoczecia;
    }

    public Timestamp getDataZakonczenia() {
        return dataZakonczenia;
    }

    public void setDataZakonczenia(Timestamp dataZakonczenia) {
        this.dataZakonczenia = dataZakonczenia;
    }

    public BigDecimal getRabat() {
        return rabat;
    }

    public void setRabat(BigDecimal rabat) {
        this.rabat = rabat;
    }

    public BigDecimal getCenaKoncowa() {
        return cenaKoncowa;
    }

    public void setCenaKoncowa(BigDecimal cenaKoncowa) {
        this.cenaKoncowa = cenaKoncowa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zlecenie zlecenie = (Zlecenie) o;
        return idZlecenia == zlecenie.idZlecenia &&
                Objects.equals(opis, zlecenie.opis) &&
                Objects.equals(dataRozpoczecia, zlecenie.dataRozpoczecia) &&
                Objects.equals(dataZakonczenia, zlecenie.dataZakonczenia) &&
                Objects.equals(rabat, zlecenie.rabat) &&
                Objects.equals(cenaKoncowa, zlecenie.cenaKoncowa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idZlecenia, opis, dataRozpoczecia, dataZakonczenia, rabat, cenaKoncowa);
    }
}
