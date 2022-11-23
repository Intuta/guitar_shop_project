package com.myproject.guitar_shop.repository;

import com.myproject.guitar_shop.domain.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    Iterable<Transaction> findAllByUserId(int userId);

}
