package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.service.CartService;
import com.myproject.guitar_shop.utility.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService service;

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/cart")
    public String cart(Model model) {
        User currentUser = CurrentUserProvider.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("cart", service.getCartByUserId(currentUser.getId()));
        }
        return "cart";
    }

}
