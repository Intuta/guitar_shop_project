package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.Item;
import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ItemService extends AppService<Item> {
    private final ItemRepository repository;
    private final CartService cartService;

    @Autowired
    public ItemService(ItemRepository repository, CartService cartService) {
        super(repository);
        this.repository = repository;
        this.cartService = cartService;
    }

    @Override
    public Item create(Item item) {
        total(item);
        return repository.save(item);
    }

    @Override
    public Item update(Item item) {
        int id = item.getId();
        if (repository.existsById(id)) {
            total(item);
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

    /**
     * @param item The method counts final cost of the item
     */
    private void total(Item item) {
        item.setSum(item.getPrice() * item.getQuantity());
    }

    public void addItem(Product product, User user) {
        Cart cart = cartService.getCartByUserId(user.getId());
        cart = cartService.create(cart);
        Item item = Item.builder().cartId(cart.getId()).product(product).price(product.getPrice()).quantity(1).build();
        List<Item> items = cart.getItems();

        if (items.contains(item)) {
            Item previousItem = items.get(items.indexOf(item));
            previousItem.setPrice(item.getPrice());
            previousItem.setQuantity(previousItem.getQuantity() + 1);
            update(previousItem);
        } else {
            item.setCartId(cart.getId());
            create(item);
            items.add(item);
        }

        cartService.update(cart);

    }

}
