package dev.voltstro.assessment2.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name="customer_order_item")
public class CustomerOrderItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(optional = false)
    private CustomerOrder customerOrder;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(optional = false)
    private Product product;

    private int quantity;

    public Long getId() {
        return id;
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
