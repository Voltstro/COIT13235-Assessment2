package dev.voltstro.assessment2.controllers;

import dev.voltstro.assessment2.entities.Customer;
import dev.voltstro.assessment2.entities.CustomerOrder;
import dev.voltstro.assessment2.entities.CustomerOrderItem;
import dev.voltstro.assessment2.entities.Product;
import dev.voltstro.assessment2.models.NewProductModel;
import dev.voltstro.assessment2.services.CustomerService;
import dev.voltstro.assessment2.services.OrderItemService;
import dev.voltstro.assessment2.services.OrderService;
import dev.voltstro.assessment2.services.ProductsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Controller for order endpoints (/order/**)
 */
@Controller
public class OrderController {

    private final CustomerService customerService;
    private final ProductsService productsService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    public OrderController(CustomerService customerService, ProductsService productsService, OrderService orderService, OrderItemService orderItemService) {
        this.customerService = customerService;
        this.productsService = productsService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    /**
     * Orders Index Page
     */
    @GetMapping("/orders/")
    public String index(@RequestParam(required = false) Long customerId, Model model) {

        List<CustomerOrder> orders;
        if(customerId == null)
            orders = orderService.getAllOrders();
        else
            orders = orderService.getAllOrders(customerId);

        model.addAttribute("orders", orders);
        return "orders/index";
    }

    /**
     * Order Edit Page
     */
    @GetMapping(value = {"/orders/edit/", "/orders/edit/{id}/"})
    public String edit(@PathVariable(required = false) Long id, Model model) {
        boolean isNew = id == null;
        model.addAttribute("newOrder", isNew);

        //Get all customers
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);

        String returnUrl;
        CustomerOrder order;

        //If new order, then set returnUrl to have no ID in it, and product to new
        if(isNew) {
            order = new CustomerOrder();
            order.setTotalCost(0);
            returnUrl = "/orders/edit/";
        } else { //Existing order, set order and returnUrl accordingly
            order = orderService.getOrderById(id);
            returnUrl = "/orders/edit/" + id + "/";

            //Product was null, just go back to main page
            if(order == null)
                return "redirect:/orders/";

            //Get all products
            model.addAttribute("products", productsService.getAllEnabledProducts());

            //Model, for submitting new items
            NewProductModel productModel = new NewProductModel();
            productModel.setQuantity(1);
            model.addAttribute("newProduct", productModel);

            //Get all order items
            model.addAttribute("orderItems", orderItemService.getAllOrderItemsByOrder(order));
        }

        model.addAttribute("order", order);
        model.addAttribute("returnUrl", returnUrl);

        return "orders/edit";
    }

    /**
     * Order Edit Post
     */
    @PostMapping(value = {"/orders/edit/", "/orders/edit/{id}/"})
    public String edit(@PathVariable(required = false) Long id, @ModelAttribute("order") CustomerOrder order) {

        if (id != null) {
            //Recreate order, so ref is the same, and the persistence will actually save
            order = orderService.getOrderById(id).copyFrom(order);
        } else { //New order
            order.setCreatedDate(new Date());
            order = orderService.saveOrder(order);
            return "redirect:/orders/edit/" + order.getId() + "/";
        }

        orderService.saveOrder(order);
        return "redirect:/orders/";
    }

    /**
     * Order Edit Add Product Post
     */
    @PostMapping("/orders/edit/{orderId}/products/add/")
    public String addProduct(@PathVariable Long orderId, @ModelAttribute("newProduct") NewProductModel productModel) {
        //Get order
        CustomerOrder order = orderService.getOrderById(orderId);

        //Order is not valid
        if(order == null)
            return "redirect:/orders/";

        //Make sure quantity is valid
        Integer quantity = productModel.getQuantity();
        if(quantity == null || quantity < 1)
            return "redirect:/orders/edit/" + order.getId() + "/";

        //Get product
        Product product = productsService.getProductById(productModel.getProductId());

        //Product is not valid
        if(product == null)
            return "redirect:/orders/";

        //Add order item
        CustomerOrderItem newOrderItem = new CustomerOrderItem();
        newOrderItem.setCustomerOrder(order);
        newOrderItem.setProduct(product);
        newOrderItem.setQuantity(productModel.getQuantity());
        orderItemService.saveOrderItem(newOrderItem);

        //Update total cost
        order.setTotalCost(calculateOrderTotalCost(order));
        orderService.saveOrder(order);

        return "redirect:/orders/edit/" + order.getId() + "/";
    }

    /**
     * Order Edit Product Remove
     */
    @GetMapping("/orders/edit/{orderId}/products/remove/{orderItemId}/")
    public String removeProduct(@PathVariable Long orderId, @PathVariable Long orderItemId) {
        //Get order item
        CustomerOrder order = orderService.getOrderById(orderId);

        //Order is not valid
        if(order == null)
            return "redirect:/orders/";

        //Get order item
        CustomerOrderItem orderItem = orderItemService.getOrderItemById(orderItemId);

        //Order item is not valid
        if(orderItem == null)
            return "redirect:/orders/";

        //Remove order item
        orderItemService.removeOrderItem(orderItem);

        //Update total cost
        order.setTotalCost(calculateOrderTotalCost(order));
        orderService.saveOrder(order);

        return "redirect:/orders/edit/" + orderId + "/";
    }

    /**
     * Order delete endpoint
     */
    @GetMapping("/orders/delete/{orderId}/")
    public String deleteOrder(@PathVariable Long orderId, Model model) {
        CustomerOrder order = orderService.getOrderById(orderId);

        //Make sure order is real
        if(order == null)
            return "redirect:/orders/";

        model.addAttribute("orderId", order.getId());
        return "/orders/delete";
    }

    /**
     * Order delete confirmed endpoint
     */
    @GetMapping("/orders/delete/{orderId}/confirm/")
    public String deleteOrderConfirm(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return "redirect:/orders/";
    }

    private Integer calculateOrderTotalCost(CustomerOrder order) {

        //We will recalculate total cost from scratch ever single time.
        //If a Product changes it cost, minus/adding will have issues
        //This guarantees it to be accurate
        List<CustomerOrderItem> items = orderItemService.getAllOrderItemsByOrder(order);
        int totalCost = 0;
        for(CustomerOrderItem orderItem : items) {
            totalCost += orderItem.getProduct().getCost() * orderItem.getQuantity();
        }

        return totalCost;
    }
}
