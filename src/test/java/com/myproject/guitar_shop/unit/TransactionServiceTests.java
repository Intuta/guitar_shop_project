package com.myproject.guitar_shop.unit;

import com.myproject.guitar_shop.domain.*;
import com.myproject.guitar_shop.exception.EmptyCartException;
import com.myproject.guitar_shop.repository.TransactionRepository;
import com.myproject.guitar_shop.service.ItemService;
import com.myproject.guitar_shop.service.TransactionService;
import com.myproject.guitar_shop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.transaction.NotSupportedException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;
    @Captor
    ArgumentCaptor<Transaction> captor;
    private Transaction transaction;
    private Cart cart;

    @BeforeEach
    public void initialization() {
        Item item = Item.builder()
                .id(1)
                .cartId(1)
                .product(Product.builder().id(1).build())
                .quantity(1)
                .price(10.0)
                .build();
        cart = Cart.builder()
                .id(1)
                .user(User.builder().id(1).build())
                .items(new ArrayList<>(Arrays.asList(item)))
                .sum(100.0)
                .build();
        transaction = Transaction.builder()
                .id(1)
                .user(User.builder().id(1).build())
                .sum(100.0)
                .items(new ArrayList<>(Arrays.asList(item)))
                .build();
    }

    @Test
    public void getTransactionById_Pass() {
        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));

        Transaction returnedTransaction = transactionService.getById(transaction.getId());

        assertThat(returnedTransaction).isEqualTo(transaction).usingRecursiveComparison();
    }

    @Test
    public void getTransactionById_Fail() {
        when(transactionRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.getById(transaction.getId())).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void getAllTransactionsByUserIdTest() {
        int userId = 1;
        List<Transaction> transactions = new ArrayList<>(List.of(transaction));

        when(transactionRepository.findAllByUserId(userId)).thenReturn(transactions);

        List<Transaction> returnedTransactions = transactionService.getAllTransactionsByUserId(userId);

        assertThat(returnedTransactions).isEqualTo(transactions).usingRecursiveComparison();
    }

    @Test
    public void createTransactionTest_Pass() {
        when(userService.getById(cart.getUser().getId())).thenReturn(cart.getUser());
        when(transactionRepository.save(captor.capture())).thenReturn(transaction);

        transactionService.createTransaction(cart);
        Transaction capturedTransaction = captor.getValue();
        capturedTransaction.setId(1);
        capturedTransaction.setItems(transaction.getItems());
        transaction.setCreationDate(capturedTransaction.getCreationDate());

        assertThat(capturedTransaction).isEqualTo(transaction).usingRecursiveComparison();
    }

    @Test
    public void createTransactionTest_Fall() {
        cart.setItems(Collections.emptyList());

        assertThatThrownBy(() -> transactionService.createTransaction(cart)).isInstanceOf(EmptyCartException.class);
    }

    @Test
    public void deleteTest_Fall() {
        assertThatThrownBy(() -> transactionService.delete(transaction)).isInstanceOf(NotSupportedException.class);
    }
}
