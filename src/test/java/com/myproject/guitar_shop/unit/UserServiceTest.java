package com.myproject.guitar_shop.unit;

import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.exception.IncorrectPasswordException;
import com.myproject.guitar_shop.exception.NonExistentUserException;
import com.myproject.guitar_shop.exception.NonUniqueEmailException;
import com.myproject.guitar_shop.repository.UserRepository;
import com.myproject.guitar_shop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    @Captor
    ArgumentCaptor<User> captor;
    @Mock
    private PasswordEncoder passwordEncoder;
    private Map<String, String> attributes;
    private String email;
    private User user;


    @BeforeEach
    public void initialization() {
        attributes = new HashMap<>();
        attributes.put("name", "name");
        attributes.put("email", "email");
        attributes.put("phone", "phone");
        attributes.put("role", "CUSTOMER");

        email = "email@domain.com";
        user = User.builder()
                .id(1)
                .name("name")
                .email(email)
                .build();
    }


    @Test
    public void getByIdTest_Pass() {
        int userId = user.getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User returnedUser = userService.getById(userId);

        assertThat(returnedUser).isEqualTo(user).usingRecursiveComparison();
    }

    @Test
    public void getUserByIdTest_Fail() {
        int userId = user.getId();

        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(userId)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void getUserByEmailTest_Pass() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User returnedUser = userService.getUserByEmail(email);

        assertThat(returnedUser).isEqualTo(user).usingRecursiveComparison();
    }

    @Test
    public void getUserByEmailTest_Fail() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByEmail(email)).isInstanceOf(NonExistentUserException.class);
    }

    @Test
    public void getAllUsersByAttributeTest() {
        List<User> users = new ArrayList<>(List.of(user));

        when(userRepository.findAllUsersByEmailOrName(email, email)).thenReturn(users);

        List<User> returnedUsers = userService.getAllUsersByAttribute(email);

        assertThat(returnedUsers).isEqualTo(users).usingRecursiveComparison();
    }

    @Test
    public void updateTest_Pass() {
        int userId = user.getId();
        User expectedUser = User.builder()
                .id(1)
                .name(attributes.get("name"))
                .email(attributes.get("email"))
                .phone(attributes.get("phone"))
                .role(User.Role.CUSTOMER)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(captor.capture())).thenReturn(expectedUser);

        User returnedUser = userService.update(attributes, userId);
        User capturedUser = captor.getValue();

        assertThat(returnedUser).isEqualTo(expectedUser).usingRecursiveComparison();
        assertThat(capturedUser).isEqualTo(expectedUser).usingRecursiveComparison();
    }

    @Test
    public void updateEmailCaseTest_Fall() {
        int userId = user.getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenThrow(new NonUniqueEmailException());

        assertThatThrownBy(() -> userService.update(attributes, userId)).isInstanceOf(NonUniqueEmailException.class);
    }

    @Test
    public void updatePasswordCaseTest_Fall() {
        int userId = user.getId();
        attributes.put("password", "password");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(attributes.get("password"), user.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> userService.update(attributes, userId)).isInstanceOf(IncorrectPasswordException.class);
    }

    @Test
    public void mapUserTest() {
        User expectedUser = User.builder()
                .name(attributes.get("name"))
                .email(attributes.get("email"))
                .phone(attributes.get("phone"))
                .role(User.Role.CUSTOMER)
                .build();

        when(userRepository.save(captor.capture())).thenReturn(expectedUser);

        User returnedUser = userService.mapUser(attributes);
        User capturedUser = captor.getValue();

        assertThat(returnedUser).isEqualTo(expectedUser).usingRecursiveComparison();
        assertThat(capturedUser).isEqualTo(expectedUser).usingRecursiveComparison();
    }

}
