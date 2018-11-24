package pl.edu.pwsztar.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
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

    // definicja relacji/mapowania (wielu Zleceń do jednego Klienta)
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_klienta")
    private Klient klient;

    // definicja relacji/mapowania (wielu Zleceń do jednego Pojazdu)
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_pojazdu")
    private Pojazd pojazd;

    // definicja relacji/mapowania (jednego Zlecenia do wielu Zadań)
    @OneToMany(mappedBy = "zlecenie", cascade = CascadeType.ALL)
    private List<Zadanie> zadania;

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

    public Klient getKlient() {
        return klient;
    }

    public void setKlient(Klient klient) {
        this.klient = klient;
    }

    public Pojazd getPojazd() {
        return pojazd;
    }

    public void setPojazd(Pojazd pojazd) {
        this.pojazd = pojazd;
    }

    public List<Zadanie> getZadania() {
        return zadania;
    }

    public void setZadania(List<Zadanie> zadania) {
        this.zadania = zadania;
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
