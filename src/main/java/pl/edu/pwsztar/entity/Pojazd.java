package pl.edu.pwsztar.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pojazd")
public class Pojazd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pojazdu", nullable = false)
    private int idPojazdu;

    @Column(name = "marka", length = 30)
    private String marka;

    @Column(name = "model", length = 20)
    private String model;

    @Column(name = "rok_produkcji")
    private Short rokProdukcji;

    @Column(name = "numer_vin", length = 17)
    private String numerVin;

    @Column(name = "pojemnosc_silnika")
    private Double pojemnoscSilnika;

    // definicja relacji/mapowania (jednego Pojazdu do wielu Zleceń)
    @OneToMany(mappedBy = "pojazd", cascade = CascadeType.ALL)
    private List<Zlecenie> zlecenia;

    public int getIdPojazdu() {
        return idPojazdu;
    }

    public void setIdPojazdu(int idPojazdu) {
        this.idPojazdu = idPojazdu;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Short getRokProdukcji() {
        return rokProdukcji;
    }

    public void setRokProdukcji(Short rokProdukcji) {
        this.rokProdukcji = rokProdukcji;
    }

    public String getNumerVin() {
        return numerVin;
    }

    public void setNumerVin(String numerVin) {
        this.numerVin = numerVin;
    }

    public Double getPojemnoscSilnika() {
        return pojemnoscSilnika;
    }

    public void setPojemnoscSilnika(Double pojemnoscSilnika) {
        this.pojemnoscSilnika = pojemnoscSilnika;
    }

    public List<Zlecenie> getZlecenia() {
        return zlecenia;
    }

    public void setZlecenia(List<Zlecenie> zlecenia) {
        this.zlecenia = zlecenia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pojazd pojazd = (Pojazd) o;
        return idPojazdu == pojazd.idPojazdu &&
                Objects.equals(marka, pojazd.marka) &&
                Objects.equals(model, pojazd.model) &&
                Objects.equals(rokProdukcji, pojazd.rokProdukcji) &&
                Objects.equals(numerVin, pojazd.numerVin) &&
                Objects.equals(pojemnoscSilnika, pojazd.pojemnoscSilnika);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPojazdu, marka, model, rokProdukcji, numerVin, pojemnoscSilnika);
    }
}
