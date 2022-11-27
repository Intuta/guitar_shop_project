package com.myproject.guitar_shop.unit;

import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.repository.UserRepository;
import com.myproject.guitar_shop.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

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


    @Test
    public void getByIdTest_Pass() throws Exception {
        int userId = 1;
        User user = User.builder()
                .id(userId)
                .name("name")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User returnedUser = userService.getById(userId);

        assertThat(returnedUser).isEqualTo(user).usingRecursiveComparison();
    }

    @Test
    public void getUserByIdTest_Fail() {
        int userId = 1;

        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(userId)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void getUserByEmailTest_Pass() {
        String email = "email@domain.com";
        User user = User.builder()
                .id(1)
                .name("name")
                .email(email)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User returnedUser = userService.getUserByEmail(email);

        assertThat(returnedUser).isEqualTo(user).usingRecursiveComparison();
    }

    @Test
    public void getUserByEmailTest_Fail() {
        String email = "email@domain.com";

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByEmail(email)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void createUserTest() throws Exception {
        User user = User.builder()
                .id(1)
                .name("name")
                .build();

        when(userRepository.save(user)).thenReturn(user);

        User returnedUser = userService.create(user);

        assertThat(returnedUser).isEqualTo(user).usingRecursiveComparison();
    }

    @Test
    public void updateUser_Pass() throws Exception {
        User user = User.builder()
                .id(1)
                .name("name")
                .build();

        when(userRepository.save(user)).thenReturn(user);

        User returnedUser = userService.update(user);

        assertThat(returnedUser).isEqualTo(user).usingRecursiveComparison();
    }

}
