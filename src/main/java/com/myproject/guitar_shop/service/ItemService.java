package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.Item;
import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.exception.NonExistentItemException;
import com.myproject.guitar_shop.exception.NotEnoughProductException;
import com.myproject.guitar_shop.utility.ErrorMessages;
import com.myproject.guitar_shop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService extends AppService<Item> {
    private final ItemRepository itemRepository;
    private final ProductService productService;
    private CartService cartService;

    @Autowired
    public ItemService(ItemRepository repository, ProductService productService) {
        super(repository);
        this.itemRepository = repository;
        this.productService = productService;
    }

    @Autowired
    @Lazy
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    public List<Item> getAllItemsByCartId(int cartId) {
        List<Item> items = new ArrayList<>();
        itemRepository.findAllByCartId(cartId).forEach(items::add);
        return items;
    }

    public List<Item> getAllItemsByTransactionId(int transactionId) {
        List<Item> items = new ArrayList<>();
        itemRepository.findAllByTransactionId(transactionId).forEach(items::add);
        return items;
    }

    /**
     * @param product Product which should be converted to Item
     * @param user    User who require to add Item
     *                The method receives the cart via user id and constructs and adds new Item to it
     */
    @Transactional
    public void addItem(Product product, User user) {
        Cart cart = cartService.getCartByUserId(user.getId());
        Item item = Item.builder().cartId(cart.getId()).product(product).price(product.getPrice()).quantity(1).build();
        cartService.addItemIntoCart(item, cart);
    }

    /**
     * The method removes Item from the Cart if quantity < 1 or set new quantity value if possible
     *
     * @param id       id of Item which quantity should be changed
     * @param quantity quantity that should be set for the Item
     * @throws NonExistentItemException  will be thrown if it is not possible to find Item by granted id
     * @throws NotEnoughProductException will be thrown if the quantity of product is not enough for set required quantity for the Item
     */
    @Transactional
    public void updateQuantity(int id, int quantity) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NonExistentItemException(ErrorMessages.ITEM_NOT_FOUND));
        Cart cart = cartService.getById(item.getCartId());
        cart.getItems().remove(item);
        if (quantity < 1) {
            itemRepository.delete(item);
        } else {
            if (quantity <= item.getProduct().getQuantity()) {
                item.setQuantity(quantity);
                save(item);
                cart.getItems().add(item);
                cartService.save(cart);
            } else {
                throw new NotEnoughProductException(String.format(ErrorMessages.NOT_ENOUGH_PRODUCTS, item.getProduct().getQuantity()));
            }
        }
    }

    /**
     * @param items         List of Items
     * @param transactionId id of transaction that should be set for items from the list
     */
    @Transactional
    public void setTransactionId(List<Item> items, int transactionId) {
        for (Item item : items) {
            item.getProduct().setQuantity(item.getProduct().getQuantity() - item.getQuantity());
            productService.save(item.getProduct());
            item.setTransactionId(transactionId);
            item.setCartId(null);
            save(item);
        }
    }

    /**
     * @return true if quantity value of newItem is less than quantity value of its product
     */
    public boolean quantityIsAvailable(Item item, int quantity) {
        return quantity <= item.getProduct().getQuantity();
    }
}
