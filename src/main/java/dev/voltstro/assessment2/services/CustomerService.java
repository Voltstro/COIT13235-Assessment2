package dev.voltstro.assessment2.services;

import dev.voltstro.assessment2.entities.Customer;
import dev.voltstro.assessment2.entities.Product;
import dev.voltstro.assessment2.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository productRepository) {
        this.customerRepository = productRepository;
    }

    public Customer getCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElse(null);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Customer> getAllCustomers(String searchName) {
        //Ideally you would be doing this in WHERE SQL clause on the DB.
        List<Customer> allProducts = getAllCustomers();

        ArrayList<Customer> foundCustomers = new ArrayList<>();
        for (Customer product : allProducts) {
            if(product.getFirstName().contains(searchName)) {
                foundCustomers.add(product);
                continue;
            }

            if(product.getLastName().contains(searchName)) {
                foundCustomers.add(product);
                continue;
            }
        }

        return foundCustomers;
    }

    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }
}
