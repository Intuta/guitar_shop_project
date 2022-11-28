package com.myproject.guitar_shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class GuitarShopApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GuitarShopApplication.class, args);
    }

}
