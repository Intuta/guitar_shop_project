package com.myproject.guitar_shop.repository;

import com.myproject.guitar_shop.domain.*;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AppRepository<T> extends CrudRepository<T, Integer> {
    Optional<Cart> findByUserId(int userId);
    Iterable<Transaction> findAllByUserId(int userId);
    Optional<User> findByEmail(String email);
    Iterable<Item> findAllByCartId(int cartId);
    Iterable<Item> findAllByTransactionId(int transactionId);
    Iterable<Product> findAllByCategory(Product.Category category);
    Optional<Product> findByTitle(String title);
}
