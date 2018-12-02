package pl.edu.pwsztar.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id", nullable = false)
    private int clientId;

    @Column(name = "first_name", length = 30)
    private String firstName;

    @Column(name = "last_name", length = 40)
    private String lastName;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "password", length = 64)
    private String password;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "hash", length = 64)
    private String hash;

    @Column(name = "is_activated")
    private Byte isActivated;

    // definicja relacji/mapowania (jednego Klienta do wielu Zleceń)
    @OneToMany(mappedBy = "client", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Job> jobs;

    {
        jobs = new ArrayList<>();
    }

    public Client() {
    }

    public Client(String firstName, String lastName, String email, String phoneNumber, Byte isActivated) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isActivated = isActivated;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Byte getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(Byte isActivated) {
        this.isActivated = isActivated;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    // dodanie metody ułatwiającej ustawienie listy Zleceń dla Klienta i jego Pojazdu
    public void addJobs(List<Job> jobs, Vehicle vehicle) {
        for (Job job: jobs) {
            job.setClient(this);
            job.setVehicle(vehicle);
            this.jobs.add(job);
        }
    }

    // dodanie metody ułatwiającej przypisywanie Zlecenia do Klienta oraz Pojazdu
    public void addJob(Job job, Vehicle vehicle) {
        job.setClient(this);
        job.setVehicle(vehicle);
        this.jobs.add(job);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return clientId == client.clientId &&
                Objects.equals(firstName, client.firstName) &&
                Objects.equals(lastName, client.lastName) &&
                Objects.equals(email, client.email) &&
                Objects.equals(password, client.password) &&
                Objects.equals(phoneNumber, client.phoneNumber) &&
                Objects.equals(hash, client.hash) &&
                Objects.equals(isActivated, client.isActivated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, firstName, lastName, email, password, phoneNumber, hash, isActivated);
    }

    @Override
    public String toString() {
        return clientId + ": " + firstName + " " + lastName + ", email: " + email;
    }
}
