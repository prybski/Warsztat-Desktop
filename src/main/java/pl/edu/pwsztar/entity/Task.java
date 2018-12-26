package pl.edu.pwsztar.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "task", schema = "garage")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "activity", nullable = false, length = 150)
    private String activity;

    @Column(name = "is_finished")
    private boolean isFinished;

    @Column(name = "cost", precision = 2)
    private BigDecimal cost;

    // definicja relacji/mapowania (wielu Zadań do jednego Zlecenia)
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    private Job job;

    // definicja relacji/mapowania (jednego Zadania do wielu Zapotrzebowań)
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Demand> demands;

    {
        this.demands = new ArrayList<>();
    }

    public Task() {
    }

    public Task(String activity) {
        this.activity = activity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public List<Demand> getDemands() {
        return demands;
    }

    public void setDemands(List<Demand> demands) {
        this.demands = demands;
    }

    // dodanie metody ułatwiającej przypisywanie Zapotrzebowania (Części dla Zadania) do Zadania
    public void addDemand(Demand demand, Part part) {
        demand.setTask(this);
        demand.setPart(part);
        this.demands.add(demand);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                Objects.equals(activity, task.activity) &&
                Objects.equals(isFinished, task.isFinished) &&
                Objects.equals(cost, task.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, activity, isFinished, cost);
    }

    @Override
    public String toString() {
        return activity;
    }
}
