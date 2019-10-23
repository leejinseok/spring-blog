package com.wonder.blog.controller;

import com.wonder.blog.entity.User;
import com.wonder.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {
  private final UserRepository userRepository;

  @Autowired
  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping
  public @ResponseBody Iterable<User> getAllUsers() {
    return userRepository.findAll();
  }
}
