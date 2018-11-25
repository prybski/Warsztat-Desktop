package pl.edu.pwsztar.entity;

import javax.persistence.*;
import java.util.ArrayList;
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

    @Column(name = "haslo", length = 64)
    private String haslo;

    @Column(name = "telefon", length = 15)
    private String telefon;

    @Column(name = "hash", length = 64)
    private String hash;

    @Column(name = "jest_aktywowany")
    private Byte jestAktywowany;

    // definicja relacji/mapowania (jednego Klienta do wielu Zleceń)
    @OneToMany(mappedBy = "klient", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Zlecenie> zlecenia;

    {
        zlecenia = new ArrayList<>();
    }

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

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Byte getJestAktywowany() {
        return jestAktywowany;
    }

    public void setJestAktywowany(Byte jestAktywowany) {
        this.jestAktywowany = jestAktywowany;
    }

    public List<Zlecenie> getZlecenia() {
        return zlecenia;
    }

    public void setZlecenia(List<Zlecenie> zlecenia) {
        this.zlecenia = zlecenia;
    }

    // dodanie metody ułatwiającej ustawienie listy Zleceń dla Klienta i jego Pojazdu
    public void addZlecenia(List<Zlecenie> zlecenia, Pojazd pojazd) {
        for (Zlecenie zlecenie: zlecenia) {
            zlecenie.setKlient(this);
            zlecenie.setPojazd(pojazd);
            this.zlecenia.add(zlecenie);
        }
    }

    // dodanie metody ułatwiającej przypisywanie Zlecenia do Klienta oraz Pojazdu
    public void addZlecenie(Zlecenie zlecenie, Pojazd pojazd) {
        zlecenie.setKlient(this);
        zlecenie.setPojazd(pojazd);
        this.zlecenia.add(zlecenie);
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

    @Override
    public String toString() {
        return "Klient {" +
                "idKlienta = " + idKlienta +
                ", imie = '" + imie + '\'' +
                ", nazwisko = '" + nazwisko + '\'' +
                ", email = '" + email + '\'' +
                ", haslo = '" + haslo + '\'' +
                ", telefon = '" + telefon + '\'' +
                ", hash = '" + hash + '\'' +
                ", jestAktywowany = " + jestAktywowany +
                '}';
    }
}
