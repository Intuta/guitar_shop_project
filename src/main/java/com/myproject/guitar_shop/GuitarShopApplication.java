package com.myproject.guitar_shop;

import com.myproject.guitar_shop.repository.ProductRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GuitarShopApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GuitarShopApplication.class, args);
        ProductRepository productRepository = context.getBean(ProductRepository.class);
        System.out.println(productRepository.findAll());
    }

}
