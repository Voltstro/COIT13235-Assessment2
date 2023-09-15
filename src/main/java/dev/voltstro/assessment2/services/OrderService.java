package dev.voltstro.assessment2.services;

import dev.voltstro.assessment2.entities.CustomerOrder;
import dev.voltstro.assessment2.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository productRepository) {
        this.orderRepository = productRepository;
    }

    public CustomerOrder saveOrder(CustomerOrder product) {
        return orderRepository.save(product);
    }

    public CustomerOrder getOrderById(Long id) {
        Optional<CustomerOrder> product = orderRepository.findById(id);
        return product.orElse(null);
    }

    public List<CustomerOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<CustomerOrder> getAllOrders(Long customerId) {
        return orderRepository.findOrderByCustomerId(customerId);
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
