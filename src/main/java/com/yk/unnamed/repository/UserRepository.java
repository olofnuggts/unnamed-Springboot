package com.yk.unnamed.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yk.unnamed.model.Token;
import com.yk.unnamed.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByTokens(Token tokens);

}
