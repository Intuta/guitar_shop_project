package com.myproject.guitar_shop.repository;

import com.myproject.guitar_shop.domain.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE users.email LIKE CONCAT('%',:email,'%') OR users.name LIKE CONCAT('%',:name,'%')")
    List<User> findAllUsersByEmailOrName(String email, String name);

}
