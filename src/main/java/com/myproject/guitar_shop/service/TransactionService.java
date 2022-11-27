package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.Transaction;
import com.myproject.guitar_shop.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.NotSupportedException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService extends AppService<Transaction> {

    private final AppRepository<Transaction> repository;

    @Autowired
    public TransactionService(AppRepository<Transaction> repository) {
        super(repository);
        this.repository = repository;
    }

    public List<Transaction> getAllTransactionsByUserId(int userId) {
        List<Transaction> receivedTransactions = new ArrayList<>();
        repository.findAllByUserId(userId).forEach(receivedTransactions::add);
        return receivedTransactions;
    }

    @Override
    public Transaction update(Transaction entity) throws NotSupportedException {
        throw new NotSupportedException("Not supported operation!");
    }

    @Override
    public void delete(Transaction entity) throws NotSupportedException {
        throw new NotSupportedException("Not supported operation!");
    }
}
