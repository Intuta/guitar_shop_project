package com.myproject.guitar_shop.repository;

import com.myproject.guitar_shop.domain.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {
    Iterable<Item> findAllByCartId(int cartId);
    Iterable<Item> findAllByTransactionId(int transactionId);
}
