package pl.edu.pwsztar.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "demand")
public class Demand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "demand_id", nullable = false)
    private int demandId;

    @Column(name = "quantity")
    private Byte quantity;

    @Column(name = "price", precision = 2)
    private BigDecimal price;

    // definicja relacji/mapowania (wielu Zapotrzebowań do jednego Zadania)
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "task_id")
    private Task task;

    // definicja relacji/mapowania (wielu Zapotrzebowań do jednej Części)
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "part_id")
    private Part part;

    public Demand() {
    }

    public Demand(Byte quantity, BigDecimal price) {
        this.quantity = quantity;
        this.price = price;
    }

    public int getDemandId() {
        return demandId;
    }

    public void setDemandId(int demandId) {
        this.demandId = demandId;
    }

    public Byte getQuantity() {
        return quantity;
    }

    public void setQuantity(Byte quantity) {
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
        return demandId == demand.demandId &&
                Objects.equals(quantity, demand.quantity) &&
                Objects.equals(price, demand.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(demandId, quantity, price);
    }

    @Override
    public String toString() {
        return "Demand {" +
                "demandId = " + demandId +
                ", quantity = " + quantity +
                ", price = " + price +
                '}';
    }
}
