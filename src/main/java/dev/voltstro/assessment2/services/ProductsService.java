package dev.voltstro.assessment2.services;

import dev.voltstro.assessment2.entities.Product;
import dev.voltstro.assessment2.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductsService {
    private ProductRepository productRepository;

    public ProductsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAllEnabledProducts() {
        return productRepository.getAllEnabledProducts();
    }

    public List<Product> getAllProducts(String searchName) {
        //Ideally you would be doing this in WHERE SQL clause on the DB, but IDK how to search names like this in mysql
        List<Product> allProducts = getAllProducts();

        ArrayList<Product> foundProducts = new ArrayList<>();
        for (Product product : allProducts) {
            if(product.getName().contains(searchName)) {
                foundProducts.add(product);
                continue;
            }

            if(product.getCode().contains(searchName)) {
                foundProducts.add(product);
                continue;
            }
        }

        return foundProducts;
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}
