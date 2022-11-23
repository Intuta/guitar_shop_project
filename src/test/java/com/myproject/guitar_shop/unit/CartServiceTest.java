package com.myproject.guitar_shop.unit;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.Item;
import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.repository.CartRepository;
import com.myproject.guitar_shop.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
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
    @Captor
    ArgumentCaptor<Cart> cartCaptor;
    @Mock
    private CartRepository cartRepository;
    @InjectMocks
    private CartService cartService;

    @BeforeEach
    public void initialization() {
        item1 = Item.builder()
                .id((int) (Math.random() * 9))
                .cartId(1)
                .product(new Product())
                .build();
        item2 = Item.builder()
                .id((int) (Math.random() * 9))
                .cartId(1)
                .product(new Product())
                .build();
        cart = Cart.builder()
                .id(1)
                .build();
    }


    @Test
    public void getCartById_Pass() {
        int id = cart.getId();

        when(cartRepository.findById(id)).thenReturn(Optional.of(cart));

        Cart returnedCart = cartService.getCartById(id);

        assertThat(returnedCart).isEqualTo(cart).usingRecursiveComparison();
    }

    @Test
    public void getCartById_Fail() {
        int id = cart.getId();

        when(cartRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.getCartById(id)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void getCartByUserIdTest_Pass() {
        int userId = 1;

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        Cart returnedCart = cartService.getCartByUserId(userId);

        assertThat(returnedCart).isEqualTo(cart).usingRecursiveComparison();
    }

    @Test
    public void getCartByUserIdTest_Fail() {
        int userId = 1;

        when(cartRepository.findByUserId(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.getCartByUserId(userId)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void createCartTest() {
        List<Item> items = new ArrayList<>();
        item1.setSum(2.0);
        item2.setSum(6.0);
        items.add(item1);
        cart.setItems(items);

        when(cartRepository.save(cart)).thenReturn(cart);

        Cart returnedCart = cartService.createCart(cart);

        assertThat(returnedCart).isEqualTo(cart).usingRecursiveComparison();
    }

    @Test
    public void updateCartTest() {
        List<Item> items = new ArrayList<>();
        item1.setSum(3.0);
        item2.setSum(3.0);
        items.add(item1);
        items.add(item2);
        cart.setItems(items);
        double expectedSum = 6.0;

        when(cartRepository.existsById(cart.getId())).thenReturn(true);
        when(cartRepository.save(cartCaptor.capture())).thenReturn(cart);

        Cart returnedCart = cartService.updateCart(cart);
        Cart capturedCart = cartCaptor.getValue();

        assertThat(capturedCart.getSum()).isEqualTo(expectedSum);
        assertThat(returnedCart).isEqualTo(cart).usingRecursiveComparison();
    }
}
