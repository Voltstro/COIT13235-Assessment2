package dev.voltstro.assessment2.repositories;

import dev.voltstro.assessment2.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM assignment02.product WHERE enabled = 1")
    public List<Product> getAllEnabledProducts();
}
