package pl.edu.pwsztar.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "demand", schema = "garage")
public class Demand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "quantity", nullable = false)
    private byte quantity;

    @Column(name = "price", nullable = false, precision = 2)
    private BigDecimal price;

    // definicja relacji/mapowania (wielu Zapotrzebowań do jednego Zadania)
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

    // definicja relacji/mapowania (wielu Zapotrzebowań do jednej Części)
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "part_id", referencedColumnName = "id")
    private Part part;

    public Demand() {
    }

    public Demand(byte quantity, BigDecimal price) {
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getQuantity() {
        return quantity;
    }

    public void setQuantity(byte quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Demand demand = (Demand) o;
        return id == demand.id &&
                Objects.equals(quantity, demand.quantity) &&
                Objects.equals(price, demand.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, price);
    }

    @Override
    public String toString() {
        return "Cena: " + price + " zł, " + part.getName() + ", ilość: " + quantity + ", nazwa zadania: "
                + task.getActivity();
    }
}
