package pl.edu.pwsztar.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "czesc")
public class Czesc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_czesci", nullable = false)
    private int idCzesci;

    @Column(name = "nazwa", length = 80)
    private String nazwa;

    @Column(name = "szczegoly", length = 100)
    private String szczegoly;

    @Column(name = "numer_katalogowy", length = 18)
    private String numerKatalogowy;

    // definicja relacji/mapowania (jednej Części do wielu Zapotrzebowań)
    @OneToMany(mappedBy = "czesc", cascade = CascadeType.ALL)
    private List<Zapotrzebowanie> zapotrzebowanie;

    {
        this.zapotrzebowanie = new ArrayList<>();
    }

    public Czesc() {
    }

    public Czesc(String nazwa, String szczegoly, String numerKatalogowy) {
        this.nazwa = nazwa;
        this.szczegoly = szczegoly;
        this.numerKatalogowy = numerKatalogowy;
    }

    public int getIdCzesci() {
        return idCzesci;
    }

    public void setIdCzesci(int idCzesci) {
        this.idCzesci = idCzesci;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getSzczegoly() {
        return szczegoly;
    }

    public void setSzczegoly(String szczegoly) {
        this.szczegoly = szczegoly;
    }

    public String getNumerKatalogowy() {
        return numerKatalogowy;
    }

    public void setNumerKatalogowy(String numerKatalogowy) {
        this.numerKatalogowy = numerKatalogowy;
    }

    public List<Zapotrzebowanie> getZapotrzebowanie() {
        return zapotrzebowanie;
    }

    public void setZapotrzebowanie(List<Zapotrzebowanie> zapotrzebowanie) {
        this.zapotrzebowanie = zapotrzebowanie;
    }

    // dodanie metody ułatwiającej przypisywanie Zapotrzebowania (Zadania dla Części) do Części
    public void addZapotrzebowanie(Zapotrzebowanie zapotrzebowanie, Zadanie zadanie) {
        zapotrzebowanie.setCzesc(this);
        zapotrzebowanie.setZadanie(zadanie);
        this.zapotrzebowanie.add(zapotrzebowanie);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Czesc czesc = (Czesc) o;
        return idCzesci == czesc.idCzesci &&
                Objects.equals(nazwa, czesc.nazwa) &&
                Objects.equals(szczegoly, czesc.szczegoly) &&
                Objects.equals(numerKatalogowy, czesc.numerKatalogowy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCzesci, nazwa, szczegoly, numerKatalogowy);
    }

    @Override
    public String toString() {
        return "Czesc {" +
                "idCzesci = " + idCzesci +
                ", nazwa = '" + nazwa + '\'' +
                ", szczegoly = '" + szczegoly + '\'' +
                ", numerKatalogowy = '" + numerKatalogowy + '\'' +
                '}';
    }
}
