package com.myproject.guitar_shop;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.Item;
import com.myproject.guitar_shop.repository.CartRepository;
import com.myproject.guitar_shop.repository.ProductRepository;
import com.myproject.guitar_shop.repository.UserRepository;
import com.myproject.guitar_shop.service.CartService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class GuitarShopApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GuitarShopApplication.class, args);
        ProductRepository productRepository = context.getBean(ProductRepository.class);
        UserRepository userRepository = context.getBean(UserRepository.class);
        CartService service = context.getBean(CartService.class);
        Cart cart = service.getCartById(1);
        service.updateCart(cart);
    }

}
