package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @param <T> Common class with realization of shared methods of services
 */
abstract class AppService<T> {
    public AppRepository<T> repository;

    @Autowired
    public AppService(AppRepository<T> repository) {
        this.repository = repository;
    }

    public T getById(int id) {
        Optional<T> receivedCart = repository.findById(id);
        return receivedCart.orElseThrow(() -> new NoSuchElementException("Not found!"));
    }

    public T create(T entity) throws Exception {
        return repository.save(entity);
    }

    public T update(T entity) throws Exception {
        return repository.save(entity);
    }

    public void delete(T entity) throws Exception {
        repository.delete(entity);
    }
}
