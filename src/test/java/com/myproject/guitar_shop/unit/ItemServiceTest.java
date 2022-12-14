package com.myproject.guitar_shop.unit;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.Item;
import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.repository.ItemRepository;
import com.myproject.guitar_shop.service.CartService;
import com.myproject.guitar_shop.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CartService cartService;
    @InjectMocks
    private ItemService itemService;
    @Captor
    ArgumentCaptor<Item> captor;
    private Item item;
    private Cart cart;

    @BeforeEach
    public void initialization() {
        item = Item.builder()
                .id(1)
                .cartId(1)
                .product(Product.builder().quantity(2).build())
                .quantity(1)
                .build();
        cart = Cart.builder()
                .id(1)
                .items(new ArrayList<>(Arrays.asList(item)))
                .build();

        itemService.setCartService(cartService);
    }

    @Test
    public void getItemByIdTest_Pass() {
        int id = item.getId();

        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        Item returnedItem = itemService.getById(id);

        assertThat(returnedItem).isEqualTo(item).usingRecursiveComparison();
    }

    @Test
    public void getItemByIdTest_Fail() {
        int id = item.getId();

        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.getById(id)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void getAllItemsByCartIdTest() {
        int cartId = 1;
        List<Item> items = new ArrayList<>(List.of(item));

        when(itemRepository.findAllByCartId(cartId)).thenReturn(items);

        List<Item> returnedItems = itemService.getAllItemsByCartId(cartId);

        assertThat(returnedItems).isEqualTo(items).usingRecursiveComparison();
    }

    @Test
    public void getAllItemsByTransactionIdTest() {
        int transactionId = 1;
        List<Item> items = new ArrayList<>(List.of(item));

        when(itemRepository.findAllByTransactionId(transactionId)).thenReturn(items);

        List<Item> returnedItems = itemService.getAllItemsByTransactionId(transactionId);

        assertThat(returnedItems).isEqualTo(items).usingRecursiveComparison();
    }

    @Test
    public void createItemTest() {
        item.setPrice(25.0);
        item.setQuantity(2);

        when(itemRepository.save(captor.capture())).thenReturn(item);

        Item returnedItem = itemService.save(item);
        assertThat(returnedItem).isEqualTo(item).usingRecursiveComparison();
    }

    @Test
    public void updateItemTest() {
        item.setPrice(30.0);
        item.setQuantity(2);

        when(itemRepository.save(captor.capture())).thenReturn(item);

        Item returnedItem = itemService.save(item);

        assertThat(returnedItem).isEqualTo(item).usingRecursiveComparison();
    }

    @Test
    public void updateQuantityTest() {
        item.setPrice(30.0);
        item.setQuantity(1);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(cartService.getById(item.getCartId())).thenReturn(cart);
        when(itemRepository.save(captor.capture())).thenReturn(item);

        itemService.updateQuantity(item.getId(), 2);
        Item capturedItem = captor.getValue();
        item.setQuantity(2);

        assertThat(capturedItem).isEqualTo(item).usingRecursiveComparison();
    }

}
