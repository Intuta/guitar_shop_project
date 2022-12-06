package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.service.CartService;
import com.myproject.guitar_shop.service.ItemService;
import com.myproject.guitar_shop.utility.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService service;
    private final ItemService itemService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/cart")
    public String cart(Model model) {
        User currentUser = CurrentUserProvider.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("cart", service.getCartByUserId(currentUser.getId()));
        }
        return "cart";
    }


    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/updateQuantity/{item_id}")
    public String updateQuantity(@PathVariable("item_id") String itemId, @RequestParam("quantity") String quantity, Model model) {
        User currentUser = CurrentUserProvider.getCurrentUser();

        itemService.updateQuantity(Integer.parseInt(itemId), Integer.parseInt(quantity));

        if (currentUser != null) {
            Cart currentCart = service.getCartByUserId(currentUser.getId());
            model.addAttribute("cart", currentCart);
        }
        return "cart";
    }

}
