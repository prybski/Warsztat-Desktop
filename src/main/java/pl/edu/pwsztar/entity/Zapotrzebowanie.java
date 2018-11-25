package pl.edu.pwsztar.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "zapotrzebowanie")
public class Zapotrzebowanie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zapotrzebowania", nullable = false)
    private int idZapotrzebowania;

    @Column(name = "ilosc")
    private Byte ilosc;

    @Column(name = "cena", precision = 2)
    private BigDecimal cena;

    // definicja relacji/mapowania (wielu Zapotrzebowań do jednego Zadania)
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_zadania")
    private Zadanie zadanie;

    // definicja relacji/mapowania (wielu Zapotrzebowań do jednej Części)
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_czesci")
    private Czesc czesc;

    public Zapotrzebowanie() {
    }

    public Zapotrzebowanie(Byte ilosc, BigDecimal cena) {
        this.ilosc = ilosc;
        this.cena = cena;
    }

    public int getIdZapotrzebowania() {
        return idZapotrzebowania;
    }

    public void setIdZapotrzebowania(int idZapotrzebowania) {
        this.idZapotrzebowania = idZapotrzebowania;
    }

    public Byte getIlosc() {
        return ilosc;
    }

    public void setIlosc(Byte ilosc) {
        this.ilosc = ilosc;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public Zadanie getZadanie() {
        return zadanie;
    }

    public void setZadanie(Zadanie zadanie) {
        this.zadanie = zadanie;
    }

    public Czesc getCzesc() {
        return czesc;
    }

    public void setCzesc(Czesc czesc) {
        this.czesc = czesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zapotrzebowanie that = (Zapotrzebowanie) o;
        return idZapotrzebowania == that.idZapotrzebowania &&
                Objects.equals(ilosc, that.ilosc) &&
                Objects.equals(cena, that.cena);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idZapotrzebowania, ilosc, cena);
    }

    @Override
    public String toString() {
        return "Zapotrzebowanie {" +
                "idZapotrzebowania = " + idZapotrzebowania +
                ", ilosc = " + ilosc +
                ", cena = " + cena +
                '}';
    }
}
