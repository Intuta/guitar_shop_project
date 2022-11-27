package com.myproject.guitar_shop.service;

import org.springframework.data.repository.CrudRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @param <T> Common class with realization of shared methods of services
 */
abstract class AppService<T> {
    public CrudRepository<T, Integer> repository;

    public AppService(CrudRepository<T, Integer> repository) {
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
