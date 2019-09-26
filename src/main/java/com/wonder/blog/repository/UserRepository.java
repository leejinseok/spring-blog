package com.wonder.blog.repository;


import com.wonder.blog.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
  User findByEmail(String email);
}