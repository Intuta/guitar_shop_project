package com.myproject.guitar_shop.integration;

import com.myproject.guitar_shop.GuitarShopApplication;
import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.Item;
import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.domain.User;
import com.myproject.guitar_shop.exception.NotEnoughProductException;
import com.myproject.guitar_shop.service.CartService;
import com.myproject.guitar_shop.service.ItemService;
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

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = GuitarShopApplication.class)
@Sql(scripts = "/test-schema.sql")
public class ItemServiceTests {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    private Product product;
    private User user;
    private Item item;

    @Before
    public void initialization() {
        product = Product.builder()
                .title("title")
                .category(Product.Category.ELECTRIC_GUITAR)
                .price(1000.0)
                .quantity(1)
                .build();
        productService.save(product);

        item = Item.builder()
                .product(product)
                .price(1000.00)
                .quantity(2)
                .sum(2000.00)
                .build();

        user = User.builder()
                .name("name")
                .email("email")
                .role(User.Role.CUSTOMER)
                .password("password")
                .build();
        userService.save(user);
    }

    @Test
    public void addItemTest() {
        Item expectedItem = Item.builder()
                .id(1)
                .cartId(1)
                .product(product)
                .price(product.getPrice())
                .quantity(1)
                .sum(product.getPrice())
                .build();

        itemService.addItem(product, user);
        Item returnedItem = itemService.getById(1);

        assertThat(returnedItem).isEqualTo(expectedItem);
    }

    @Test
    public void updateQuantityTest() {
        Cart cart = cartService.getCartByUserId(user.getId());
        cart = cartService.save(cart);
        item.setCartId(cart.getId());
        itemService.save(item);

        Item expectedItem = item;
        expectedItem.setQuantity(1);

        itemService.updateQuantity(item.getId(), 1);
        Item returnedItem = itemService.getById(item.getId());

        assertThat(returnedItem).isEqualTo(expectedItem).usingRecursiveComparison();
        assertThatThrownBy(() -> itemService.updateQuantity(item.getId(), 10)).isInstanceOf(NotEnoughProductException.class);

        itemService.updateQuantity(item.getId(), 0);
        assertThatThrownBy(() -> itemService.getById(item.getId())).isInstanceOf(NoSuchElementException.class);
    }

}
