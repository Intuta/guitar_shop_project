package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.service.CartService;
import com.myproject.guitar_shop.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;
    private final CartService cartService;


    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/buy/{cartId}")
    public String commitTransaction(@PathVariable String cartId) throws Exception {
        Cart currentCart = cartService.getById(Integer.parseInt(cartId));
        service.createTransaction(currentCart);
        cartService.delete(currentCart);
        return "home";
    }
}
