package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService extends AppService<Product> {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository repository) {
        super(repository);
        this.productRepository = repository;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products;
    }

    public List<Product> getProductByTitle(String title) {
        return productRepository.findAllByTitleContaining(title);
    }

    public List<Product> getAllProductsByCategory(Product.Category category) {
        List<Product> products = new ArrayList<>();
        productRepository.findAllByCategory(category).forEach(products::add);
        return products;
    }

    public Product update(Product product) {
        return productRepository.save(product);
    }

}
