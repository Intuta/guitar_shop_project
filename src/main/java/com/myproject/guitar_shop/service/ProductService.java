package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService extends AppService<Product> {

    private final AppRepository<Product> repository;

    @Autowired
    public ProductService(AppRepository<Product> repository) {
        super(repository);
        this.repository = repository;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        repository.findAll().forEach(products::add);
        return products;
    }

    public Product getProductByTitle(String title) {
        Optional<Product> receivedProduct = repository.findByTitle(title);
        return receivedProduct.orElseThrow(() -> new NoSuchElementException(String.format("Product with title %s not found", title)));
    }

    public List<Product> getAllProductsByCategory(Product.Category category) {
        List<Product> products = new ArrayList<>();
        repository.findAllByCategory(category).forEach(products::add);
        return products;
    }

}
