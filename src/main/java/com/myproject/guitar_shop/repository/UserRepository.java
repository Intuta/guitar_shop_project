package com.myproject.guitar_shop.repository;

import com.myproject.guitar_shop.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends AppRepository<User> {

}