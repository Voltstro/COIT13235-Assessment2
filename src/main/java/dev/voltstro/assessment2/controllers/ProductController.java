package dev.voltstro.assessment2.controllers;

import dev.voltstro.assessment2.Utils;
import dev.voltstro.assessment2.entities.Product;
import dev.voltstro.assessment2.services.ProductsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for /products/ endpoints
 */
@Controller
public class ProductController {

    private final ProductsService productsService;

    /**
     * Creates a new ProductController instance
     */
    public ProductController(ProductsService productsService) {
        this.productsService = productsService;
    }

    /**
     * Products index endpoint
     */
    @GetMapping("/products/")
    public String index(@RequestParam(required = false) String search, Model model) {
        model.addAttribute("search", search);

        //Get products
        List<Product> productList;
        if(Utils.isNullOrEmpty(search))
            productList = productsService.getAllProducts();
        else
            productList = productsService.getAllProducts(search);

        model.addAttribute("products", productList);

        return "products/index";
    }

    /**
     * Products edit endpoint
     */
    @GetMapping(value = {"/products/edit/", "/products/edit/{id}/"})
    public String edit(@PathVariable(required = false) Long id, Model model) {
        boolean isNew = id == null;
        model.addAttribute("newProduct", isNew);

        String returnUrl;
        Product product;

        //If new product, then set returnUrl to have no ID in it, and product to new
        if(isNew) {
            product = new Product();
            product.setEnabled(true);
            returnUrl = "/products/edit/";
        } else { //Existing product, set product and returnUrl accordingly
            product = productsService.getProductById(id);
            returnUrl = "/products/edit/" + id + "/";

            //Product was null, just go back to main page
            if(product == null)
                return "redirect:/products/";
        }

        model.addAttribute("returnUrl", returnUrl);
        model.addAttribute("product", product);
        return "products/edit";
    }

    /**
     * Products edit post endpoint
     */
    @PostMapping(value = {"/products/edit/", "/products/edit/{id}/"})
    public String edit(@PathVariable(required = false) Long id, @ModelAttribute("product") Product product) {

        if (id != null) {
            //Recreate product, so ref is the same, and the persistence will actually save
            product = productsService.getProductById(id).copyFrom(product);
        }

        productsService.saveProduct(product);
        return "redirect:/products/";
    }

    /**
     * Products delete endpoint
     */
    @GetMapping("/products/delete/{productId}/")
    public String deleteProduct(@PathVariable Long productId, Model model) {
        Product product = productsService.getProductById(productId);

        //Make sure product is real
        if(product == null)
            return "redirect:/products/";

        model.addAttribute("productId", product.getId());
        return "/products/delete";
    }

    /**
     * Products delete confirmed endpoint
     */
    @GetMapping("/products/delete/{productId}/confirm/")
    public String deleteProductConfirm(@PathVariable Long productId) {
        productsService.deleteProduct(productId);
        return "redirect:/products/";
    }
}
