package com.myproject.guitar_shop.repository;

import com.myproject.guitar_shop.domain.Product;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
    Iterable<Product> findAllByCategory(Product.Category category);

    @Query(value = "SELECT * FROM products WHERE products.title  LIKE CONCAT('%',:title,'%')")
    List<Product> findAllByTitleContaining(@Param("title") String title);
}
