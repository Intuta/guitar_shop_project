package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService extends AppService<User> {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) {
        Optional<User> receivedUser = repository.findByEmail(email);
        return receivedUser.orElseThrow(() -> new NoSuchElementException(String.format("User with email %s not found", email)));
    }

    public User mapUser(Map<String, String> userInfo) {
        return User.builder()
                .name(userInfo.get("firstName"))
                .email(URLDecoder.decode(userInfo.get("email"), StandardCharsets.UTF_8))
                .phone(userInfo.get("phone"))
                .password(passwordEncoder.encode(userInfo.get("password")))
                .role(User.Role.valueOf("CUSTOMER"))
                .build();

    }

}
