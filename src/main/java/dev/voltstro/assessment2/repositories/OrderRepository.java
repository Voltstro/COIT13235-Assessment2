package dev.voltstro.assessment2.repositories;

import dev.voltstro.assessment2.entities.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
}
