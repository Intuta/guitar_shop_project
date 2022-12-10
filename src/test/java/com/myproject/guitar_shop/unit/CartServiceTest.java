package com.myproject.guitar_shop.unit;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.Item;
import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.repository.CartRepository;
import com.myproject.guitar_shop.service.CartService;
import com.myproject.guitar_shop.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
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
                .product(new Product())
                .build();
        item2 = Item.builder()
                .id(2)
                .cartId(1)
                .product(new Product())
                .build();
        cart = Cart.builder()
                .id(1)
                .items(Arrays.asList(item1, item2))
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
    public void updateCartTest() {
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart returnedCart = cartService.save(cart);
        assertThat(returnedCart).isEqualTo(cart).usingRecursiveComparison();
    }

}