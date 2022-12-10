package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.exception.NonExistentItemException;
import com.myproject.guitar_shop.exception.NotEnoughProductException;
import com.myproject.guitar_shop.service.CartService;
import com.myproject.guitar_shop.service.ItemService;
import com.myproject.guitar_shop.utility.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final ItemService itemService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/cart")
    public String cart(Model model) {
        User currentUser = CurrentUserProvider.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("cart", cartService.getCartByUserId(currentUser.getId()));
        }
        return "home";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/updateQuantity/{item_id}")
    public String updateQuantity(@PathVariable("item_id") String itemId, @RequestParam("quantity") String quantity, Model model) {
        User currentUser = CurrentUserProvider.getCurrentUser();

        itemService.updateQuantity(Integer.parseInt(itemId), Integer.parseInt(quantity));

        if (currentUser != null) {
            Cart currentCart = cartService.getCartByUserId(currentUser.getId());
            model.addAttribute("cart", currentCart);
        }
        return "home";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NotEnoughProductException.class, NonExistentItemException.class})
    public String handleException(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

}
