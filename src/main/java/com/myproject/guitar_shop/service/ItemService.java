package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.Item;
import com.myproject.guitar_shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository repository;

    public Item getItemById(int id) {
        Optional<Item> receivedItem = repository.findById(id);
        return receivedItem.orElseThrow(() -> new NoSuchElementException(String.format("Item with id %s not found", id)));
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

    public Item createItem(Item item) {
        total(item);
        return repository.save(item);
    }

    public Item updateItem(Item item) {
        int id = item.getId();
        if (repository.existsById(id)) {
            total(item);
            return repository.save(item);
        }
        else {
            throw new NoSuchElementException(String.format("Item with id %s not found", id));
        }
    }

    public void deleteItem(Item item) {
        repository.delete(item);
    }

    /**
     * @param item
     * The method counts final cost of the item
     */
    private void total(Item item) {
        item.setSum(item.getPrice() * item.getQuantity());
    }
}
