package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.Item;
import com.myproject.guitar_shop.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class CartService extends AppService<Cart> {
    private final CartRepository repository;
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public CartService(CartRepository repository, UserService userService, ItemService itemService) {
        super(repository);
        this.repository = repository;
        this.userService = userService;
        this.itemService = itemService;
    }


    public Cart getCartByUserId(int userId) {
        Optional<Cart> receivedCart = repository.findByUserId(userId);
        if (receivedCart.isEmpty()) {
            Cart cart = Cart.builder().user(userService.getById(userId)).items(new ArrayList<>()).build();
            return save(cart);
        } else return receivedCart.get();
    }

    @Override
    public Cart save(Cart cart) {
        return repository.save(cart);
    }


    public Cart update(Cart cart) {
        return repository.save(cart);
    }

    /**
     * @param item
     * @param cart The method receives List of Items of the cart and checks if it contains item.
     *             If it does - it updates price-information and adds 1 unit
     *             If it doesn't - adds this new item into the cart
     */
    public void addItemIntoCart(Item item, Cart cart) throws Exception {
        List<Item> items = cart.getItems();
        if (items.contains(item)) {
            Item itemInCart = items.get(items.indexOf(item));
            if (itemInCart.getQuantity() < itemInCart.getProduct().getQuantity()) {
                itemInCart.setPrice(item.getPrice());
                itemInCart.setQuantity(itemInCart.getQuantity() + 1);
                itemService.update(itemInCart);
            }
            else throw new Exception();
        } else {
            item.setCartId(cart.getId());
            itemService.save(item);
            items.add(item);
        }
        update(cart);
    }

    public List<Item> removeItemsIfAbsent(Cart cart) {
        List<Item> items = cart.getItems();
        items.removeIf(item -> item.getQuantity() > item.getProduct().getQuantity());
        update(cart);
        return items;
    }

}
