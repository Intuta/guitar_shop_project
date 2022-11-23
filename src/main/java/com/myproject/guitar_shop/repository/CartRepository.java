package com.myproject.guitar_shop.repository;

import com.myproject.guitar_shop.domain.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends CrudRepository<Cart, Integer> {
    Optional<Cart> findByUserId(int userId);
}
