package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CartController extends AbstractController {

    private final CartService service;

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/cart")
    public String cart(Model model) {
        User currentUser = this.getCurrentUser();
        model.addAttribute("cart", service.getCartByUserId(currentUser.getId()));
        model.addAttribute("user", this.getCurrentUser());
        return "cart";
    }

}
