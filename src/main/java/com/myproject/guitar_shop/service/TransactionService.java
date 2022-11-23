package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.Transaction;
import com.myproject.guitar_shop.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;

    public Transaction getTransactionById(int id) {
        Optional<Transaction> receivedTransaction = repository.findById(id);
        return receivedTransaction.orElseThrow(() -> new NoSuchElementException(String.format("Transaction with id %s not found", id)));
    }

    public List<Transaction> getAllTransactionsByUserId(int userId) {
        List<Transaction> receivedTransactions = new ArrayList<>();
        repository.findAllByUserId(userId).forEach(receivedTransactions::add);
        return receivedTransactions;
    }

    public Transaction createTransaction(Transaction transaction) {
        return repository.save(transaction);
    }
}
