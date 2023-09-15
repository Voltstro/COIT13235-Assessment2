package dev.voltstro.assessment2.controllers;

import dev.voltstro.assessment2.Utils;
import dev.voltstro.assessment2.entities.Customer;
import dev.voltstro.assessment2.services.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CustomerController {
    private final CustomerService productsService;

    /**
     * Creates a new ProductController instance
     */
    public CustomerController(CustomerService productsService) {
        this.productsService = productsService;
    }

    @GetMapping("/customers/")
    public String index(@RequestParam(required = false) String search, Model model) {
        model.addAttribute("search", search);

        //Get customers
        List<Customer> customers;
        if(Utils.isNullOrEmpty(search))
            customers = productsService.getAllCustomers();
        else
            customers = productsService.getAllCustomers(search);

        model.addAttribute("customers", customers);

        return "customers/index";
    }

    @GetMapping(value = {"/customers/edit/", "/customers/edit/{id}/"})
    public String edit(@PathVariable(required = false) Long id, Model model) {
        boolean isNew = id == null;
        model.addAttribute("newCustomer", isNew);

        String returnUrl;
        Customer customer;

        //If new customer, then set returnUrl to have ID in it, and customer to new
        if(isNew) {
            customer = new Customer();
            returnUrl = "/customers/edit/";
        } else { //Existing customer, set customer and returnUrl accordingly
            customer = productsService.getCustomerById(id);
            returnUrl = "/customers/edit/" + id + "/";

            //Customer was null, just go back to main page
            if(customer == null)
                return "redirect:/customers/";
        }

        model.addAttribute("returnUrl", returnUrl);
        model.addAttribute("customer", customer);
        return "customers/edit";
    }

    @PostMapping(value = {"/customers/edit/", "/customers/edit/{id}/"})
    public String edit(@PathVariable(required = false) Long id, @ModelAttribute("product") Customer customer) {

        if (id != null) {
            //Recreate customer, so ref is the same, and the persistence will actually save
            customer = productsService.getCustomerById(id).copyFrom(customer);
        }

        productsService.saveCustomer(customer);
        return "redirect:/customers/";
    }

    @GetMapping("/customers/delete/{customerId}/")
    public String deleteCustomer(@PathVariable Long customerId, Model model) {
        Customer customer = productsService.getCustomerById(customerId);

        //Make sure customer is real
        if(customer == null)
            return "redirect:/customers/";

        model.addAttribute("orderId", customer.getId());
        return "/customers/delete";
    }

    @GetMapping("/customers/delete/{customerId}/confirm/")
    public String deleteCustomerConfirm(@PathVariable Long customerId) {
        productsService.deleteCustomer(customerId);
        return "redirect:/customers/";
    }
}
