package com.myproject.guitar_shop.integration;

import com.myproject.guitar_shop.GuitarShopApplication;
import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = GuitarShopApplication.class)
@Sql(scripts = "/test-schema.sql")
public class UserServiceTests {
    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    private User user;
    private Map<String, String> attributes;

    @Before
    public void initialization() {
        user = User.builder()
                .name("ann")
                .email("ann@email.com")
                .phone("12314")
                .password(passwordEncoder.encode("password"))
                .role(User.Role.CUSTOMER)
                .build();

        attributes = new HashMap<>();
        attributes.put("name", "name");
        attributes.put("email", "email");
        attributes.put("phone", "phone");
        attributes.put("role", "CUSTOMER");
        attributes.put("password", "password");
        attributes.put("new_password", "new_password");
    }

    @Test
    public void saveTest() {
        assertThat(userService.save(user)).isEqualTo(user).usingRecursiveComparison();
    }

    @Test
    public void getUserByEmailTest() {
        userService.save(user);
        User returnedUser = userService.getUserByEmail(user.getEmail());
        assertThat(returnedUser).isEqualTo(user).usingRecursiveComparison();
    }

    @Test
    public void getAllUsersByAttributeTest() {
        userService.save(user);
        List<User> expectedUserList = new ArrayList<>(Collections.singletonList(user));

        List<User> returnedUserList = userService.getAllUsersByAttribute(user.getName());
        assertThat(returnedUserList).isEqualTo(expectedUserList);

        returnedUserList = userService.getAllUsersByAttribute(user.getEmail());
        assertThat(returnedUserList).isEqualTo(expectedUserList);
    }

    @Test
    public void updateTest() {
        userService.save(user);
        User expectedUser = User.builder()
                .name(attributes.get("name"))
                .email(attributes.get("email"))
                .phone(attributes.get("phone"))
                .role(User.Role.valueOf(attributes.get("role")))
                .password(attributes.get("new_password")).build();

        User returnedUser = userService.update(attributes, user.getId());
        expectedUser.setId(returnedUser.getId());
        expectedUser.setPassword(returnedUser.getPassword());

        assertThat(returnedUser).isEqualTo(expectedUser).usingRecursiveComparison();
    }

    @Test
    public void mapUser() {
        User expectedUser = User.builder()
                .name(attributes.get("name"))
                .email(attributes.get("email"))
                .phone(attributes.get("phone"))
                .role(User.Role.valueOf(attributes.get("role")))
                .password(attributes.get("password")).build();

        User returnedUser = userService.createUser(attributes);
        expectedUser.setId(returnedUser.getId());
        expectedUser.setPassword(returnedUser.getPassword());

        assertThat(returnedUser).isEqualTo(expectedUser).usingRecursiveComparison();
    }

}
