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
}
