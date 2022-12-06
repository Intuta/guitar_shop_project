package com.myproject.guitar_shop.repository;

import com.myproject.guitar_shop.domain.Item;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {
    Iterable<Item> findAllByCartId(int cartId);
    Iterable<Item> findAllByTransactionId(int transactionId);
}
