package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.utility.ErrorMessages;
import org.springframework.data.repository.CrudRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Common class with realization of shared methods of services
 */
abstract class AppService<T> {
    public CrudRepository<T, Integer> repository;

    public AppService(CrudRepository<T, Integer> repository) {
        this.repository = repository;
    }

    public T getById(int id) {
        Optional<T> receivedElement = repository.findById(id);
        return receivedElement.orElseThrow(() -> new NoSuchElementException(ErrorMessages.ELEMENT_NOT_FOUND));
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public void delete(T entity) throws Exception {
        repository.delete(entity);
    }
}
