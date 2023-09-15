package dev.voltstro.assessment2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Main page controller
 */
@Controller
public class MainController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
