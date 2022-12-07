package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.Item;
import com.myproject.guitar_shop.domain.Transaction;
import com.myproject.guitar_shop.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.NotSupportedException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService extends AppService<Transaction> {

    private final TransactionRepository repository;
    private final ItemService itemService;
    private final UserService userService;

    @Autowired
    public TransactionService(TransactionRepository repository, UserService userService, ItemService itemService) {
        super(repository);
        this.repository = repository;
        this.userService = userService;
        this.itemService = itemService;
    }

    public List<Transaction> getAllTransactionsByUserId(int userId) {
        List<Transaction> receivedTransactions = new ArrayList<>();
        repository.findAllByUserId(userId).forEach(receivedTransactions::add);
        return receivedTransactions;
    }

    public void createTransaction(Cart cart) {
        Transaction transaction = Transaction.builder()
                .user(userService.getById(cart.getUser().getId()))
                .sum(cart.getSum())
                .creationDate(Date.valueOf(LocalDate.now()))
                .build();

        transaction = save(transaction);
        itemService.setTransactionId(cart.getItems(), transaction.getId());
    }

    public Transaction update(Transaction entity) throws NotSupportedException {
        throw new NotSupportedException("Not supported operation!");
    }

    @Override
    public void delete(Transaction entity) throws NotSupportedException {
        throw new NotSupportedException("Not supported operation!");
    }
}
