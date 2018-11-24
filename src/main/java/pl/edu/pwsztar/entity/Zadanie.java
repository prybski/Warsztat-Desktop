package pl.edu.pwsztar.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "zadanie")
public class Zadanie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zadania", nullable = false)
    private int idZadania;

    @Column(name = "czynnosc", length = 100)
    private String czynnosc;

    @Column(name = "przewidywany_czas", length = 12)
    private String przewidywanyCzas;

    @Column(name = "jest_zakonczone")
    private Byte jestZakonczone;

    @Column(name = "koszt", precision = 2)
    private BigDecimal koszt;

    // definicja relacji/mapowania (wielu Zadań do jednego Zlecenia)
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_zlecenia")
    private Zlecenie zlecenie;

    // definicja relacji/mapowania (jednego Zadania do wielu Zapotrzebowań)
    @OneToMany(mappedBy = "zadanie", cascade = CascadeType.ALL)
    private List<Zapotrzebowanie> zapotrzebowanie;

    public Zadanie() {
    }

    public Zadanie(String czynnosc, String przewidywanyCzas, Byte jestZakonczone, BigDecimal koszt) {
        this.czynnosc = czynnosc;
        this.przewidywanyCzas = przewidywanyCzas;
        this.jestZakonczone = jestZakonczone;
        this.koszt = koszt;
    }

    public int getIdZadania() {
        return idZadania;
    }

    public void setIdZadania(int idZadania) {
        this.idZadania = idZadania;
    }

    public String getCzynnosc() {
        return czynnosc;
    }

    public void setCzynnosc(String czynnosc) {
        this.czynnosc = czynnosc;
    }

    public String getPrzewidywanyCzas() {
        return przewidywanyCzas;
    }

    public void setPrzewidywanyCzas(String przewidywanyCzas) {
        this.przewidywanyCzas = przewidywanyCzas;
    }

    public Byte getJestZakonczone() {
        return jestZakonczone;
    }

    public void setJestZakonczone(Byte jestZakonczone) {
        this.jestZakonczone = jestZakonczone;
    }

    public BigDecimal getKoszt() {
        return koszt;
    }

    public void setKoszt(BigDecimal koszt) {
        this.koszt = koszt;
    }

    public Zlecenie getZlecenie() {
        return zlecenie;
    }

    public void setZlecenie(Zlecenie zlecenie) {
        this.zlecenie = zlecenie;
    }

    public List<Zapotrzebowanie> getZapotrzebowanie() {
        return zapotrzebowanie;
    }

    public void setZapotrzebowanie(List<Zapotrzebowanie> zapotrzebowanie) {
        this.zapotrzebowanie = zapotrzebowanie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zadanie zadanie = (Zadanie) o;
        return idZadania == zadanie.idZadania &&
                Objects.equals(czynnosc, zadanie.czynnosc) &&
                Objects.equals(przewidywanyCzas, zadanie.przewidywanyCzas) &&
                Objects.equals(jestZakonczone, zadanie.jestZakonczone) &&
                Objects.equals(koszt, zadanie.koszt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idZadania, czynnosc, przewidywanyCzas, jestZakonczone, koszt);
    }
}
