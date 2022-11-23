package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.Item;
import com.myproject.guitar_shop.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository repository;

    public Cart getCartById(int id) {
        Optional<Cart> receivedCart = repository.findById(id);
        return receivedCart.orElseThrow(() -> new NoSuchElementException(String.format("Cart with id %s not found", id)));
    }

    public Cart getCartByUserId(int userId) {
        Optional<Cart> receivedCart = repository.findByUserId(userId);
        return receivedCart.orElseThrow(() -> new NoSuchElementException(String.format("Cart with user id %s not found", userId)));
    }

    public Cart createCart(Cart cart) {
        calculateTotal(cart);
        return repository.save(cart);
    }

    public Cart updateCart(Cart cart) {
        int id = cart.getId();
        if (repository.existsById(id)) {
            calculateTotal(cart);
            return repository.save(cart);
        } else {
            throw new NoSuchElementException(String.format("Cart with id %s not found", id));
        }
    }

    public void deleteCart(Cart cart) {
        repository.delete(cart);
    }

    /**
     * @param cart
     * The method counts sum of all products in the cart
     */
    private void calculateTotal(Cart cart) {
        double sum = cart.getItems().stream()
                .mapToDouble(Item::getSum)
                .sum();
        cart.setSum(sum);
    }
}
