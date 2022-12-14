package com.myproject.guitar_shop.unit;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.Item;
import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.exception.NotEnoughProductException;
import com.myproject.guitar_shop.repository.CartRepository;
import com.myproject.guitar_shop.service.CartService;
import com.myproject.guitar_shop.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTests {
    private Item item1;
    private Item item2;
    private Cart cart;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ItemService itemService;
    @InjectMocks
    private CartService cartService;

    @BeforeEach
    public void initialization() {
        item1 = Item.builder()
                .id(1)
                .cartId(1)
                .product(Product.builder().id(1).build())
                .quantity(1)
                .price(10.0)
                .build();
        item2 = Item.builder()
                .id(2)
                .cartId(1)
                .product(Product.builder().id(2).build())
                .quantity(1)
                .price(10.0)
                .build();
        cart = Cart.builder()
                .id(1)
                .items(new ArrayList<>(Arrays.asList(item1, item2)))
                .build();
    }


    @Test
    public void getCartById_Pass() {
        int id = cart.getId();

        when(cartRepository.findById(id)).thenReturn(Optional.of(cart));

        Cart returnedCart = cartService.getById(id);

        assertThat(returnedCart).isEqualTo(cart).usingRecursiveComparison();
    }

    @Test
    public void getCartById_Fail() {
        int id = cart.getId();

        when(cartRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.getById(id)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void getCartByUserIdTest() {
        int userId = 1;

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        Cart returnedCart = cartService.getCartByUserId(userId);

        assertThat(returnedCart).isEqualTo(cart).usingRecursiveComparison();
    }

    @Test
    public void createCartTest() {
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart returnedCart = cartService.save(cart);

        assertThat(returnedCart).isEqualTo(cart).usingRecursiveComparison();
    }

    @Test
    public void addItemIntoCartTest_Pass() {
        Item item3 = Item.builder()
                .id(3)
                .cartId(1)
                .product(Product.builder().id(3).build())
                .quantity(1)
                .price(10.0)
                .build();
        List<Item> expectedItemList = Arrays.asList(item1, item2, item3);
        cartService.addItemIntoCart(item3, cart);
        List<Item> returnedItemList = cart.getItems();

        assertThat(returnedItemList).isEqualTo(expectedItemList).usingRecursiveComparison();
    }

    @Test
    public void addItemIntoCartTest_Fall() {
        Item item3 = Item.builder()
                .id(item1.getId())
                .cartId(item1.getCartId())
                .product(item1.getProduct())
                .quantity(item1.getQuantity() + 1)
                .price(item1.getPrice())
                .build();

        when(itemService.quantityIsAvailable(item3, item3.getQuantity())).thenReturn(false);

        assertThatThrownBy(() -> cartService.addItemIntoCart(item3, cart)).isInstanceOf(NotEnoughProductException.class);
    }

    @Test
    public void updateCartTest() {
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart returnedCart = cartService.save(cart);

        assertThat(returnedCart).isEqualTo(cart).usingRecursiveComparison();
    }

}