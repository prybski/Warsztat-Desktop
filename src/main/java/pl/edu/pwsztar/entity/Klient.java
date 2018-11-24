package pl.edu.pwsztar.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "klient")
public class Klient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_klienta", nullable = false)
    private int idKlienta;

    @Column(name = "imie", length = 30)
    private String imie;

    @Column(name = "nazwisko", length = 40)
    private String nazwisko;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "telefon", length = 15)
    private String telefon;

    // definicja relacji/mapowania (jednego Klienta do wielu Zleceń)
    @OneToMany(mappedBy = "klient", cascade = CascadeType.ALL)
    private List<Zlecenie> zlecenia;

    public Klient() {
    }

    public Klient(String imie, String nazwisko, String email, String telefon) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.telefon = telefon;
    }

    public int getIdKlienta() {
        return idKlienta;
    }

    public void setIdKlienta(int idKlienta) {
        this.idKlienta = idKlienta;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
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
        Klient klient = (Klient) o;
        return idKlienta == klient.idKlienta &&
                Objects.equals(imie, klient.imie) &&
                Objects.equals(nazwisko, klient.nazwisko) &&
                Objects.equals(email, klient.email) &&
                Objects.equals(telefon, klient.telefon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idKlienta, imie, nazwisko, email, telefon);
    }
}
