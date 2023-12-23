package com.yk.unnamed.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.yk.unnamed.model.User;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);

}
