package dev.voltstro.assessment2.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import static jakarta.persistence.TemporalType.DATE;

@Entity
@Table(name="customer_order")
public class CustomerOrder implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(optional = false)
    private Customer customer;

    @Temporal(DATE)
    @DateTimeFormat(pattern="dd-MMM-YYYY")
    private Date createdDate;

    private Integer totalCost;

    private boolean fulfilled;

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customerId) {
        this.customer = customerId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public CustomerOrder copyFrom(CustomerOrder order) {
        this.customer = order.customer;
        this.totalCost = order.totalCost;
        this.fulfilled = order.fulfilled;
        return this;
    }
}
