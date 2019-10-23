package com.wonder.blog.repository;


import com.wonder.blog.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Integer> {
  Optional<User> findByEmail(String email);
}