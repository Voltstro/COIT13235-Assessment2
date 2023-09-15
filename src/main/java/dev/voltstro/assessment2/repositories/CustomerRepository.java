package dev.voltstro.assessment2.repositories;

import dev.voltstro.assessment2.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
