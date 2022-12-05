package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.service.CartService;
import com.myproject.guitar_shop.service.ItemService;
import com.myproject.guitar_shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ProductController extends AbstractController {

    private final ProductService service;
    private final ItemService itemService;

    private final CartService cartService;

    @GetMapping("/product/{id}")
    public String productById(@PathVariable String id, Model model) {
        model.addAttribute("product", service.getById(Integer.parseInt(id)));
        model.addAttribute("user", this.getCurrentUser());
        return "product";
    }


    @GetMapping("/category/{category}")
    public String category(@PathVariable String category, Model model) {
        model.addAttribute("guitars", service.getAllProductsByCategory(Product.Category.valueOf(category)));
        model.addAttribute("user", this.getCurrentUser());
        return "products";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/addIntoCart/{id}")
    public String addIntoCart(@PathVariable String id, Model model) {
        Product currentProduct = service.getById(Integer.parseInt(id));
        User currentUser = this.getCurrentUser();
        itemService.addItem(currentProduct, currentUser);

        model.addAttribute("guitars", service.getAllProductsByCategory(currentProduct.getCategory()));
        model.addAttribute("user", this.getCurrentUser());
//        model.addAttribute("itemInTheCart", cartService.getCartByUserId(currentUser.getId()).getItems());
        return "products";

    }
}
