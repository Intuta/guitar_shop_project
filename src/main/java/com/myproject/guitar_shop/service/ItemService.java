package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.Item;
import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ItemService extends AppService<Item> {
    private final ItemRepository repository;
    private CartService cartService;

    @Autowired
    public ItemService(ItemRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Autowired
    @Lazy
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public Item save(Item item) {
        return repository.save(item);
    }

    public Item update(Item item) {
        int id = item.getId();
        if (repository.existsById(id)) {
            return repository.save(item);
        } else {
            throw new NoSuchElementException(String.format("Item with id %s not found", id));
        }
    }

    public List<Item> getAllItemsByCartId(int cartId) {
        List<Item> items = new ArrayList<>();
        repository.findAllByCartId(cartId).forEach(items::add);
        return items;
    }

    public List<Item> getAllItemsByTransactionId(int transactionId) {
        List<Item> items = new ArrayList<>();
        repository.findAllByTransactionId(transactionId).forEach(items::add);
        return items;
    }

    public void updateQuantity(int id, int quantity) {
        Item item = repository.findById(id).orElseThrow(NoSuchElementException::new);
        Cart cart = cartService.getById(item.getCartId());
        cart.getItems().remove(item);
        if (quantity < 1) {
            repository.delete(item);
        } else {
            item.setQuantity(quantity);
            update(item);
            cart.getItems().add(item);
        }
        cartService.update(cart);
    }

    /**
     * @param product
     * @param user    The method receives the cart via user id and constructs a new Item
     */
    public void addItem(Product product, User user) {
        Cart cart = cartService.getCartByUserId(user.getId());
        Item item = Item.builder().cartId(cart.getId()).product(product).price(product.getPrice()).quantity(1).build();
        cartService.addItemIntoCart(item, cart);
    }

    public List<Item> setTransactionId(List<Item> items, int transactionId) {
        for (Item item:items) {
            item.setTransactionId(transactionId);
            item.setCartId(null);
            update(item);
        }
        return items;
    }
}
