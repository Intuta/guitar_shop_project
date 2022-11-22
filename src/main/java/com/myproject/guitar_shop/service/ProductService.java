package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        repository.findAll().forEach(products::add);
        return products;
    }

    public Product getProductById(int id) {
        Optional<Product> receivedProduct = repository.findById(id);
        return receivedProduct.orElseThrow(() -> new NoSuchElementException(String.format("Product with id %s not found", id)));
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

    public Product createProduct(Product product) {
        return repository.save(product);
    }

    public Product updateProduct(Product product) {
        int id = product.getId();
        if (repository.existsById(id)) {
            return repository.save(product);
        } else {
            throw new NoSuchElementException(String.format("Product with id %s not found", id));
        }
    }

    public void deleteProduct(Product product) {
        repository.delete(product);
    }

}
