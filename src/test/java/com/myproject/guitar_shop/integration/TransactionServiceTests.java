package com.myproject.guitar_shop.integration;

import com.myproject.guitar_shop.GuitarShopApplication;
import com.myproject.guitar_shop.domain.*;
import com.myproject.guitar_shop.exception.EmptyCartException;
import com.myproject.guitar_shop.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = GuitarShopApplication.class)
@Sql(scripts = "/test-schema.sql")
public class TransactionServiceTests {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ItemService itemService;
    private Cart cart;
    private Item item;
    private User user;

    @Before
    public void initialization() {
        user = User.builder()
                .name("name")
                .email("email")
                .role(User.Role.CUSTOMER)
                .password("password")
                .build();
        userService.save(user);

        Product product = Product.builder()
                .title("product")
                .category(Product.Category.ACOUSTIC_GUITAR)
                .quantity(2)
                .price(100.00)
                .build();
        productService.save(product);

        item = Item.builder()
                .product(product)
                .price(100.00)
                .quantity(2)
                .sum(200.00)
                .build();

        cart = Cart.builder()
                .user(user)
                .items(new ArrayList<>(Collections.singletonList(item)))
                .build();

        itemService.save(item);
        cartService.save(cart);
    }

    @Test
    public void createTest_Pass() {
        Transaction expectedTransaction = Transaction.builder()
                .id(1)
                .user(user)
                .items(new ArrayList<>(Collections.singletonList(item)))
                .sum(200.00)
                .build();

        transactionService.createTransaction(cart);
        Transaction returnedTransaction = transactionService.getById(1);
        expectedTransaction.setCreationDate(returnedTransaction.getCreationDate());

        assertThat(returnedTransaction).isEqualTo(expectedTransaction).usingRecursiveComparison();
    }

    @Test
    public void createTest_Fall() {
        cart.setItems(Collections.emptyList());

        assertThatThrownBy(() -> transactionService.createTransaction(cart)).isInstanceOf(EmptyCartException.class);
    }
}

