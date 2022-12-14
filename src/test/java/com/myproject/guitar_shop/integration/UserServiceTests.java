package com.myproject.guitar_shop.integration;

import com.myproject.guitar_shop.GuitarShopApplication;
import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GuitarShopApplication.class)
@SpringBootTest
@Sql(scripts = "/test-schema.sql")
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @Test
    public void setItemServiceTest() {
        userService.save(User.builder()
                .name("Ann")
                .email("Ann@email.com")
                .phone("12314")
                .password("password")
                .role(User.Role.CUSTOMER)
                .build());
    }
}
