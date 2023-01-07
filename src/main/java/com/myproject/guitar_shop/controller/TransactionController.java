package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.exception.NonExistentItemException;
import com.myproject.guitar_shop.exception.NotEnoughProductException;
import com.myproject.guitar_shop.service.CartService;
import com.myproject.guitar_shop.service.ItemService;
import com.myproject.guitar_shop.service.TransactionService;
import com.myproject.guitar_shop.utility.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final CartService cartService;
    private final ItemService itemService;

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMINISTRATOR')")
    @GetMapping("/checkout/{cartId}")
    public String checkoutTransaction(@PathVariable String cartId, Model model) {
        Cart currentCart = cartService.getById(Integer.parseInt(cartId));
        currentCart.setItems(cartService.removeItemsIfAbsent(currentCart));
        model.addAttribute("itemsForConfirmation", currentCart);
        return "home";
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMINISTRATOR')")
    @GetMapping("/submit/{cartId}")
    public String submitTransaction(@PathVariable String cartId, Model model) throws Exception {
        Cart currentCart = cartService.getById(Integer.parseInt(cartId));
        transactionService.createTransaction(currentCart);
        cartService.delete(currentCart);
        User currentUser = CurrentUserProvider.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("transactions", transactionService.getAllTransactionsByUserId(currentUser.getId()));
        }
        return "home";
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMINISTRATOR')")
    @GetMapping("")
    public String getAllTransactions(Model model) {
        User currentUser = CurrentUserProvider.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("transactions", transactionService.getAllTransactionsByUserId(currentUser.getId()));
        }
        return "home";
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMINISTRATOR')")
    @GetMapping("/{id}")
    public String getTransactionById(Model model, @PathVariable String id) {
        model.addAttribute("transactionItems", itemService.getAllItemsByTransactionId(Integer.parseInt(id)));
        return "home";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NoSuchElementException.class, NonExistentItemException.class, NotEnoughProductException.class})
    public String handleException(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}
