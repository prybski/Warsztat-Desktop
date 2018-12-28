package pl.edu.pwsztar.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "vehicle", schema = "garage")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "brand", nullable = false, length = 30)
    private String brand;

    @Column(name = "model", nullable = false, length = 20)
    private String model;

    @Column(name = "production_year", nullable = false)
    private short productionYear;

    @Column(name = "vin_number", length = 17)
    private String vinNumber;

    @Column(name = "engine_capacity")
    private float engineCapacity;

    // definicja relacji/mapowania (jednego Pojazdu do wielu Zleceń)
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Job> jobs;

    {
        jobs = new ArrayList<>();
    }

    public Vehicle() {
    }

    public Vehicle(String brand, String model, short productionYear, String vinNumber, float engineCapacity) {
        this.brand = brand;
        this.model = model;
        this.productionYear = productionYear;
        this.vinNumber = vinNumber;
        this.engineCapacity = engineCapacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public short getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(short productionYear) {
        this.productionYear = productionYear;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public float getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(float engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
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
        return id == vehicle.id &&
                Objects.equals(brand, vehicle.brand) &&
                Objects.equals(model, vehicle.model) &&
                Objects.equals(productionYear, vehicle.productionYear) &&
                Objects.equals(vinNumber, vehicle.vinNumber) &&
                Objects.equals(engineCapacity, vehicle.engineCapacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, model, productionYear, vinNumber, engineCapacity);
    }

    @Override
    public String toString() {
        return "Marka: " + brand + ", model: " + model + ", numer VIN: " + vinNumber;
    }
}
