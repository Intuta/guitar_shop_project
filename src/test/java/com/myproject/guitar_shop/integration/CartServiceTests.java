package com.myproject.guitar_shop.integration;

import com.myproject.guitar_shop.GuitarShopApplication;
import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.Item;
import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.exception.NotEnoughProductException;
import com.myproject.guitar_shop.service.CartService;
import com.myproject.guitar_shop.service.ProductService;
import com.myproject.guitar_shop.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = GuitarShopApplication.class)
@Sql(scripts = "/test-schema.sql")
public class CartServiceTests {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    private Item item;
    private User user;
    private Cart cart;

    @Before
    public void initialization() {
        Product product = Product.builder()
                .title("product")
                .category(Product.Category.ACOUSTIC_GUITAR)
                .quantity(2)
                .price(100.00)
                .build();
        productService.save(product);

        user = User.builder()
                .name("name")
                .email("email")
                .role(User.Role.CUSTOMER)
                .password("password")
                .build();
        userService.save(user);

        item = Item.builder()
                .product(product)
                .price(100.00)
                .quantity(1)
                .sum(100.00)
                .build();

        cart = cartService.getCartByUserId(user.getId());
    }

    @Test
    public void addItemIntoCartTest() {
        Cart expectedCart = cartService.getCartByUserId(user.getId());
        expectedCart.getItems().add(item);
        expectedCart.setSum(100.0);

        cartService.addItemIntoCart(item, cart);
        Cart returnedCart = cartService.getCartByUserId(user.getId());

        assertThat(returnedCart.getId()).isEqualTo(expectedCart.getId());
        assertThat(returnedCart.getUser()).isEqualTo(expectedCart.getUser());
        assertThat(returnedCart.getSum()).isEqualTo(expectedCart.getSum());

        cartService.addItemIntoCart(item, cart);
        expectedCart.getItems().add(item);
        expectedCart.setSum(200.0);
        returnedCart = cartService.getCartByUserId(user.getId());

        assertThat(returnedCart.getId()).isEqualTo(expectedCart.getId());
        assertThat(returnedCart.getUser()).isEqualTo(expectedCart.getUser());
        assertThat(returnedCart.getSum()).isEqualTo(expectedCart.getSum());

        assertThatThrownBy(() -> cartService.addItemIntoCart(item, cart)).isInstanceOf(NotEnoughProductException.class);
    }

}
