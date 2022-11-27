package com.myproject.guitar_shop.unit;

import com.myproject.guitar_shop.domain.Transaction;
import com.myproject.guitar_shop.repository.TransactionRepository;
import com.myproject.guitar_shop.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionService transactionService;
    private Transaction transaction;

    @BeforeEach
    public void initialization() {
        transaction = Transaction.builder()
                .id(1)
                .sum(100.0)
                .build();
    }

    @Test
    public void getTransactionById_Pass() throws Exception {
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
    public void createTransactionTest() throws Exception {
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction returnedTransaction = transactionService.create(transaction);

        assertThat(returnedTransaction).isEqualTo(transaction).usingRecursiveComparison();
    }
}
