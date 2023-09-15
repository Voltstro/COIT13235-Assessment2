package dev.voltstro.assessment2.services;

import dev.voltstro.assessment2.entities.CustomerOrder;
import dev.voltstro.assessment2.entities.CustomerOrderItem;
import dev.voltstro.assessment2.repositories.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {
    private OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository productRepository) {
        this.orderItemRepository = productRepository;
    }

    public void saveOrderItem(CustomerOrderItem product) {
        orderItemRepository.save(product);
    }

    public CustomerOrderItem getOrderItemById(Long id) {
        Optional<CustomerOrderItem> product = orderItemRepository.findById(id);
        return product.orElse(null);
    }

    public List<CustomerOrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    public List<CustomerOrderItem> getAllOrderItemsByOrder(CustomerOrder order) {
        return orderItemRepository.findOrderItemsByOrderId(order.getId());
    }

    public void removeOrderItem(CustomerOrderItem item) {
        orderItemRepository.delete(item);
    }
}
