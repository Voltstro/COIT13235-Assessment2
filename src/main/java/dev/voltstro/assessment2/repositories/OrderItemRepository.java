package dev.voltstro.assessment2.repositories;

import dev.voltstro.assessment2.entities.CustomerOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<CustomerOrderItem, Long> {
    @Query(nativeQuery = true, value = "SELECT * from assignment02.customer_order_item WHERE customer_order_id =:orderId")
    List<CustomerOrderItem> findOrderItemsByOrderId(@Param("orderId") Long orderId);
}
