package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.Item;
import com.myproject.guitar_shop.exception.NotEnoughProductException;
import com.myproject.guitar_shop.utility.ErrorMessages;
import com.myproject.guitar_shop.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService extends AppService<Cart> {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public CartService(CartRepository repository, UserService userService, ItemService itemService) {
        super(repository);
        this.cartRepository = repository;
        this.userService = userService;
        this.itemService = itemService;
    }

    /**
     * @param userId id of User who requires a cart
     * @return: The method returns the user`s cart if it exists.
     * If the user doesn't have a cart the method returns a new one.
     */
    public Cart getCartByUserId(int userId) {
        Optional<Cart> receivedCart = cartRepository.findByUserId(userId);
        if (receivedCart.isEmpty()) {
            Cart cart = Cart.builder().user(userService.getById(userId)).items(new ArrayList<>()).build();
            return save(cart);
        } else return receivedCart.get();
    }

    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    /**
     * The method receives a List of Items from the cart and checks if it contains items.
     * If it doesn't - adds this new item to the cart
     * If it does - it updates price and quantity information and tries to add it to the cart
     *
     * @param item Item which must be added to the cart
     * @param cart Cart for adding the item
     * @throws NotEnoughProductException will be thrown if the quantity of product is not enough for adding another item-unit
     */
    public void addItemIntoCart(Item item, Cart cart) throws NotEnoughProductException {
        int quantityToAdd = 1;
        List<Item> items = cart.getItems();
        if (items.contains(item)) {
            Item itemInCart = items.get(items.indexOf(item));
            quantityToAdd = quantityToAdd + itemInCart.getQuantity();
            if (itemService.quantityIsAvailable(item, quantityToAdd)) {
                itemInCart.setPrice(item.getPrice());
                itemInCart.setQuantity(quantityToAdd);
                itemService.save(itemInCart);
            } else
                throw new NotEnoughProductException(String.format(ErrorMessages.NOT_ENOUGH_PRODUCTS, item.getProduct().getQuantity()));
        } else {
            itemService.save(item);
            items.add(item);
        }
        save(cart);
    }

    /**
     * The method removes all items from the cart
     * whose quantity is greater than quantity of their products, saves the updated cart
     *
     * @param cart Cart from which should be removed lacking items
     * @return: Updated List<Item> of cart
     */
    public List<Item> removeItemsIfAbsent(Cart cart) {
        List<Item> items = cart.getItems();
        items.removeIf(item -> item.getQuantity() > item.getProduct().getQuantity());
        save(cart);
        return items;
    }

}
