package dev.voltstro.assessment2.repositories;

import dev.voltstro.assessment2.entities.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {

    //SELECT * FROM assignment02.customer_order co WHERE customer_id = 1;
    @Query(nativeQuery = true, value = "SELECT * FROM assignment02.customer_order WHERE customer_id =:customerId")
    List<CustomerOrder> findOrderByCustomerId(@Param("customerId") Long customerId);

}
