package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.service.CartService;
import com.myproject.guitar_shop.service.ItemService;
import com.myproject.guitar_shop.service.TransactionService;
import com.myproject.guitar_shop.utility.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final CartService cartService;

    private final ItemService itemService;


    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/buy/{cartId}")
    public String commitTransaction(@PathVariable String cartId) throws Exception {
        Cart currentCart = cartService.getById(Integer.parseInt(cartId));
        transactionService.createTransaction(currentCart);
        cartService.delete(currentCart);
        return "home";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/transactions")
    public String getAllTransactions(Model model) {
        model.addAttribute("transactions", transactionService.getAllTransactionsByUserId(CurrentUserProvider.getCurrentUser().getId()));
        return "home";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/transaction/{id}")
    public String getTransaction(Model model, @PathVariable String id) {
        model.addAttribute("transactionItems", itemService.getAllItemsByTransactionId(Integer.parseInt(id)));
        return "home";
    }
}
