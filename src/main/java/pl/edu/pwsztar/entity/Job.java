package pl.edu.pwsztar.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "job", schema = "garage")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @Column(name = "fixed_date", nullable = false)
    private Date fixedDate;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(name = "discount", precision = 2)
    private BigDecimal discount;

    // definicja relacji/mapowania (wielu Zleceń do jednego Klienta)
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    // definicja relacji/mapowania (wielu Zleceń do jednego Pojazdu)
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    // definicja relacji/mapowania (jednego Zlecenia do wielu Zadań)
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<Task> tasks;

    {
        this.tasks = new ArrayList<>();
    }

    public Job() {
    }

    public Job(String description, Date fixedDate) {
        this.description = description;
        this.fixedDate = fixedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getFixedDate() {
        return fixedDate;
    }

    public void setFixedDate(Date fixedDate) {
        this.fixedDate = fixedDate;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    // dodanie metody ułatwiającej ustawienie listy Zadań dla Zlecenia
    public void addTasks(List<Task> tasks) {
        for (Task task: tasks) {
            task.setJob(this);
            this.tasks.add(task);
        }
    }

    // dodanie metody ułatwiającej przypisywanie Zadania do Zlecenia
    public void addTask(Task task) {
        task.setJob(this);
        this.tasks.add(task);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return id == job.id &&
                Objects.equals(description, job.description) &&
                Objects.equals(fixedDate, job.fixedDate) &&
                Objects.equals(startDate, job.startDate) &&
                Objects.equals(endDate, job.endDate) &&
                Objects.equals(discount, job.discount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, fixedDate, startDate, endDate, discount);
    }

    @Override
    public String toString() {
        return id + ": " + description + ", " + fixedDate;
    }
}
