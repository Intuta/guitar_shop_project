package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User getUserById(int id) {
        Optional<User> receivedUser = repository.findById(id);
        return receivedUser.orElseThrow(() -> new NoSuchElementException(String.format("User with id %s not found", id)));
    }
    public User getUserByEmail(String email) {
        Optional<User> receivedUser = repository.findByEmail(email);
        return receivedUser.orElseThrow(() -> new NoSuchElementException(String.format("User with email %s not found", email)));
    }
    public User createUser(User user) {
        return repository.save(user);
    }

    public User updateUser(User user) {
        int id = user.getId();
        if (repository.existsById(id)) {
            return repository.save(user);
        } else {
            throw new NoSuchElementException(String.format("User with id %s not found", id));
        }
    }

    public void deleteUser(User user) {
        repository.delete(user);
    }
}
