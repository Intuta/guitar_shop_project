package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.exception.IncorrectPasswordException;
import com.myproject.guitar_shop.exception.NonExistentUserException;
import com.myproject.guitar_shop.exception.NonUniqueEmailException;
import com.myproject.guitar_shop.exception.utility.ErrorMessages;
import com.myproject.guitar_shop.repository.UserRepository;
import com.myproject.guitar_shop.utility.EmailFormatter;
import org.hibernate.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService extends AppService<User> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String PASSWORD = "password";
    public static final String NEW_PASSWORD = "new_password";

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) {
        Optional<User> receivedUser = userRepository.findByEmail(EmailFormatter.formatEmail(email));
        return receivedUser.orElseThrow(() -> new NonExistentUserException(String.format(ErrorMessages.USER_NOT_FOUND_BY_EMAIL, EmailFormatter.formatEmail(email))));
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
                    user.setName(attributes.get(NAME));
                    break;
                case EMAIL:
                    try {
                        user.setEmail(attributes.get(EMAIL));
                    } catch (NonUniqueResultException e) {
                        throw new NonUniqueEmailException(String.format(ErrorMessages.INVALID_EMAIL, attributes.get(EMAIL)));
                    }
                    break;
                case PHONE:
                    user.setPhone(attributes.get(PHONE));
                    break;
                case PASSWORD:
                    if (passwordEncoder.matches(attributes.get(PASSWORD), user.getPassword())) {
                        user.setPassword(attributes.get(NEW_PASSWORD));
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
                .name(attributes.get(NAME))
                .email(EmailFormatter.formatEmail(attributes.get(EMAIL)))
                .phone(attributes.get(PHONE))
                .password(passwordEncoder.encode(attributes.get(PASSWORD)))
                .role(User.Role.CUSTOMER)
                .build();
        try {
            return save(newUser);
        } catch (DataAccessException e) {
            throw new NonUniqueEmailException(String.format(ErrorMessages.INVALID_EMAIL, EmailFormatter.formatEmail(attributes.get(EMAIL))));
        }
    }
}
