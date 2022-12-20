package com.myproject.guitar_shop.repository;

import com.myproject.guitar_shop.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
    Iterable<Product> findAllByCategory(Product.Category category);

    @Query(value = "SELECT * FROM products WHERE products.title  LIKE CONCAT('%',:title,'%')")
    Page<Product> findAllByTitleContaining(@Param("title") String title, Pageable pageable);
}
