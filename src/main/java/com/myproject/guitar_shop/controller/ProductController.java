package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ProductController extends AbstractController {

    private final ProductService service;

    @GetMapping("/product/{id}")
    public String productById(@PathVariable String id, Model model) {
        model.addAttribute("product", service.getById(Integer.parseInt(id)));
        model.addAttribute("user", this.getCurrentUser());
        return "product";
    }


    //    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/category/{category}")
    public String category(@PathVariable String category, Model model) {
        model.addAttribute("guitars", service.getAllProductsByCategory(Product.Category.valueOf(category)));
        model.addAttribute("user", this.getCurrentUser());
        return "products";
    }
}
