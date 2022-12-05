package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.Item;
import com.myproject.guitar_shop.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service

public class CartService extends AppService<Cart> {
    private final CartRepository repository;
    private final UserService userService;

    @Autowired
    public CartService(CartRepository repository, UserService userService) {
        super(repository);
        this.repository = repository;
        this.userService = userService;
    }

    public Cart getCartByUserId(int userId) {
        Optional<Cart> receivedCart = repository.findByUserId(userId);

        if (receivedCart.isEmpty()) {
            Cart cart = Cart.builder().user(userService.getById(userId)).items(new ArrayList<>()).build();
            create(cart);
        }

        receivedCart = repository.findByUserId(userId);
        return receivedCart.get();
    }

    @Override
    public Cart create(Cart cart) {
        calculateTotal(cart);
        return repository.save(cart);
    }

    @Override
    public Cart update(Cart cart) {
        int id = cart.getId();
        if (repository.existsById(id)) {
            calculateTotal(cart);
            return repository.save(cart);
        } else {
            throw new NoSuchElementException(String.format("Cart with id %s not found", id));
        }
    }

    /**
     * @param cart The method counts sum of all items in the cart
     */
    private void calculateTotal(Cart cart) {
        double sum = cart.getItems().stream()
                .mapToDouble(Item::getSum)
                .sum();
        cart.setSum(sum);
    }
}
