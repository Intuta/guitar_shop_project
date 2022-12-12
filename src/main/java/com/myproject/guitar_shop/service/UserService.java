package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.exception.IncorrectPasswordException;
import com.myproject.guitar_shop.exception.NonExistentUserException;
import com.myproject.guitar_shop.exception.NonUniqueEmailException;
import com.myproject.guitar_shop.repository.UserRepository;
import com.myproject.guitar_shop.utility.ErrorMessages;
import com.myproject.guitar_shop.utility.TextFormatter;
import org.hibernate.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService extends AppService<User> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String ROLE = "role";
    public static final String PASSWORD = "password";
    public static final String NEW_PASSWORD = "new_password";

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) {
        Optional<User> receivedUser = userRepository.findByEmail(TextFormatter.formatText(email));
        return receivedUser.orElseThrow(() -> new NonExistentUserException(String.format(ErrorMessages.USER_NOT_FOUND_BY_EMAIL, TextFormatter.formatText(email))));
    }

    public List<User> getAllUsersByAttribute(String attribute) {
        return userRepository.findAllUsersByEmailOrName(attribute, attribute);
    }

    /**
     * @param attributes Map with attributes which should be set
     * @param userId     id of User for who attributes should be set
     * @return The Method returns updated User
     * @throws NonExistentUserException   will be thrown if it is not possible to receive User by granted id
     * @throws NonUniqueEmailException    will be thrown if it is not possible to set email for User because User with such email already exists
     * @throws IncorrectPasswordException will be thrown If attributes contain a password && it is not matched with current password of the User
     */
    public User update(Map<String, String> attributes, int userId) throws RuntimeException {
        Optional<User> currentUser = userRepository.findById(userId);
        currentUser.ifPresent(user -> attributes.keySet().forEach(key -> {
            switch (key) {
                case NAME:
                    user.setName(TextFormatter.formatText(attributes.get(NAME)));
                    break;
                case EMAIL:
                    try {
                        user.setEmail(TextFormatter.formatText(attributes.get(EMAIL)));
                    } catch (NonUniqueResultException e) {
                        throw new NonUniqueEmailException(String.format(ErrorMessages.INVALID_EMAIL, attributes.get(EMAIL)));
                    }
                    break;
                case PHONE:
                    user.setPhone(attributes.get(PHONE));
                    break;
                case ROLE:
                    user.setRole(User.Role.valueOf(attributes.get(ROLE)));
                    break;
                case PASSWORD:
                    if (passwordEncoder.matches(attributes.get(PASSWORD), user.getPassword()) && !attributes.get(NEW_PASSWORD).isEmpty()) {
                        user.setPassword(passwordEncoder.encode(attributes.get(NEW_PASSWORD)));
                    } else {
                        throw new IncorrectPasswordException(ErrorMessages.WRONG_PASSWORD);
                    }
                    break;
            }
        }));
        return save(currentUser.orElseThrow(() -> new NonExistentUserException(ErrorMessages.USER_NOT_FOUND)));
    }

    /**
     * @param attributes Map with attributes which should be set for new User
     * @return Returns new User
     * @throws NonUniqueEmailException will be thrown if it is not possible to set email for User because User with such email already exists
     */
    public User mapUser(Map<String, String> attributes) throws NonUniqueEmailException {
        User newUser = User.builder()
                .name(TextFormatter.formatText(attributes.get(NAME)))
                .email(TextFormatter.formatText(attributes.get(EMAIL)))
                .phone(attributes.get(PHONE))
                .password(passwordEncoder.encode(attributes.get(PASSWORD)))
                .role(User.Role.CUSTOMER)
                .build();
        try {
            return save(newUser);
        } catch (DataAccessException e) {
            throw new NonUniqueEmailException(String.format(ErrorMessages.INVALID_EMAIL, TextFormatter.formatText(attributes.get(EMAIL))));
        }
    }
}
