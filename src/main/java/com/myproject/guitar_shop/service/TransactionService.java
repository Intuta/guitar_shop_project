package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.Cart;
import com.myproject.guitar_shop.domain.Transaction;
import com.myproject.guitar_shop.exception.EmptyCartException;
import com.myproject.guitar_shop.utility.ErrorMessages;
import com.myproject.guitar_shop.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.NotSupportedException;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService extends AppService<Transaction> {
    private final TransactionRepository transactionRepository;
    private final ItemService itemService;
    private final UserService userService;


    @Autowired
    public TransactionService(TransactionRepository repository, UserService userService, ItemService itemService) {
        super(repository);
        this.transactionRepository = repository;
        this.userService = userService;
        this.itemService = itemService;
    }

    public List<Transaction> getAllTransactionsByUserId(int userId) {
        List<Transaction> receivedTransactions = new ArrayList<>();
        transactionRepository.findAllByUserId(userId).forEach(receivedTransactions::add);
        return receivedTransactions;
    }

    /**
     * @param cart Cart items from which should be used for creation of new Transaction
     * @throws EmptyCartException will be thrown if the cart is empty
     */
    @Transactional
    public void createTransaction(Cart cart) {
        if (!cart.getItems().isEmpty()) {
            Transaction transaction = Transaction.builder()
                    .user(userService.getById(cart.getUser().getId()))
                    .sum(cart.getSum())
                    .creationDate(Date.valueOf(LocalDate.now()))
                    .build();

            transaction = save(transaction);
            itemService.setTransactionId(cart.getItems(), transaction.getId());
        } else throw new EmptyCartException("Cart is empty!");
    }

    @Override
    public void delete(Transaction entity) throws NotSupportedException {
        throw new NotSupportedException(ErrorMessages.NOT_SUPPORTED);
    }
}
