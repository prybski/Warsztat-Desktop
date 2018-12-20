package pl.edu.pwsztar.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "part", schema = "garage")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @Column(name = "details", length = 150)
    private String details;

    @Column(name = "development_code", nullable = false, length = 18)
    private String developmentCode;

    // definicja relacji/mapowania (jednej Części do wielu Zapotrzebowań)
    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL)
    private List<Demand> demands;

    {
        this.demands = new ArrayList<>();
    }

    public Part() {
    }

    public Part(String name, String details, String developmentCode) {
        this.name = name;
        this.details = details;
        this.developmentCode = developmentCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDevelopmentCode() {
        return developmentCode;
    }

    public void setDevelopmentCode(String developmentCode) {
        this.developmentCode = developmentCode;
    }

    public List<Demand> getDemand() {
        return demands;
    }

    public void setDemand(List<Demand> demands) {
        this.demands = demands;
    }

    // dodanie metody ułatwiającej przypisywanie Zapotrzebowania (Zadania dla Części) do Części
    public void addDemand(Demand demand, Task task) {
        demand.setPart(this);
        demand.setTask(task);
        this.demands.add(demand);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Part part = (Part) o;
        return id == part.id &&
                Objects.equals(name, part.name) &&
                Objects.equals(details, part.details) &&
                Objects.equals(developmentCode, part.developmentCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, details, developmentCode);
    }

    @Override
    public String toString() {
        return "Part {" +
                "partId = " + id +
                ", name = '" + name + '\'' +
                ", details = '" + details + '\'' +
                ", developmentCode = '" + developmentCode + '\'' +
                '}';
    }
}
