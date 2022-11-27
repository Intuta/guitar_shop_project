package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService extends AppService<User> {

    private final AppRepository<User> repository;

    @Autowired
    public UserService(AppRepository<User> repository) {
        super(repository);
        this.repository = repository;
    }

    public User getUserByEmail(String email) {
        Optional<User> receivedUser = repository.findByEmail(email);
        return receivedUser.orElseThrow(() -> new NoSuchElementException(String.format("User with email %s not found", email)));
    }
}
