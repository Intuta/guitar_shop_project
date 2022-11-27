package com.myproject.guitar_shop.repository;

import com.myproject.guitar_shop.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends AppRepository<Product> {

}
