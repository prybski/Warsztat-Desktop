package pl.edu.pwsztar.entity;

import javax.persistence.*;
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
}
