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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final CartService cartService;
    private final ItemService itemService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/buy/{cartId}")
    public String confirmTransaction(@PathVariable String cartId, Model model) {
        Cart currentCart = cartService.getById(Integer.parseInt(cartId));
        currentCart.setItems(cartService.removeItemsIfAbsent(currentCart));
        model.addAttribute("itemsForConfirmation", currentCart);
        return "home";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/purchase/{cartId}")
    public String commitTransaction(@PathVariable String cartId, Model model) throws Exception {
        Cart currentCart = cartService.getById(Integer.parseInt(cartId));
        transactionService.createTransaction(currentCart);
        cartService.delete(currentCart);
        User currentUser = CurrentUserProvider.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("transactions", transactionService.getAllTransactionsByUserId(currentUser.getId()));
        }
        return "home";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/transactions")
    public String getAllTransactions(Model model) {
        User currentUser = CurrentUserProvider.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("transactions", transactionService.getAllTransactionsByUserId(currentUser.getId()));
        }
        return "home";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/transaction/{id}")
    public String getTransaction(Model model, @PathVariable String id) {
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
