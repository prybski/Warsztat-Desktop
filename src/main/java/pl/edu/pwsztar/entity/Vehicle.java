package pl.edu.pwsztar.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id", nullable = false)
    private int vehicleId;

    @Column(name = "brand", length = 30)
    private String brand;

    @Column(name = "model", length = 20)
    private String model;

    @Column(name = "production_year")
    private Short productionYear;

    @Column(name = "vin_number", length = 17)
    private String vinNumber;

    @Column(name = "engine_capacity")
    private Double engineCapacity;

    // definicja relacji/mapowania (jednego Pojazdu do wielu Zleceń)
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Job> jobs;

    // definicja relacji/mapowania (wielu Pojazdów do jednego Klienta)
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "client_id")
    private Client client;

    {
        jobs = new ArrayList<>();
    }

    public Vehicle() {
    }

    public Vehicle(String brand, String model, Short productionYear, String vinNumber, Double engineCapacity) {
        this.brand = brand;
        this.model = model;
        this.productionYear = productionYear;
        this.vinNumber = vinNumber;
        this.engineCapacity = engineCapacity;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Short getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(Short productionYear) {
        this.productionYear = productionYear;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public Double getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(Double engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // dodanie metody ułatwiającej ustawienie listy Zleceń dla Pojazdu i Klienta, do którego on należy
    public void addJobs(List<Job> jobs, Client client) {
        for (Job job: jobs) {
            job.setVehicle(this);
            job.setClient(client);
            this.jobs.add(job);
        }
    }

    // dodanie metody ułatwiającej przypisywanie Zlecenia do Pojazdu oraz Klienta
    public void addJob(Job job, Client client) {
        job.setVehicle(this);
        job.setClient(client);
        this.jobs.add(job);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return vehicleId == vehicle.vehicleId &&
                Objects.equals(brand, vehicle.brand) &&
                Objects.equals(model, vehicle.model) &&
                Objects.equals(productionYear, vehicle.productionYear) &&
                Objects.equals(vinNumber, vehicle.vinNumber) &&
                Objects.equals(engineCapacity, vehicle.engineCapacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId, brand, model, productionYear, vinNumber, engineCapacity);
    }

    @Override
    public String toString() {
        return vehicleId + ": " + brand + " " + model + ", numer VIN: " + vinNumber;
    }
}
