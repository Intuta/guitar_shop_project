package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @Autowired
    ProductService service;

    @RequestMapping(value = {"", "/home"})
    public String home() {
        return "home";
    }

    @GetMapping("/log_in")
    public String log() {
        return "log_in";
    }

    @GetMapping("/product/{id}")
    public String productById(@PathVariable String id, Model model) {
        Product receivedProduct = service.getById(Integer.parseInt(id));
        model.addAttribute("product", receivedProduct);
        return "product";
    }

    @GetMapping("/registration")
    public String register() {
        return "registration";
    }


    @GetMapping("/category/{category}")
    public String acoustic(@PathVariable String category, Model model) {
        System.out.println(category);
        model.addAttribute("guitars", service.getAllProductsByCategory(Product.Category.valueOf(category)));
        return "category";
    }

    @PostMapping("/authorization")
    public String authorize() {
        return "home";
    }

}
