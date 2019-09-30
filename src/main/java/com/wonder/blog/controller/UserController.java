package com.wonder.blog.controller;

import com.wonder.blog.entity.User;
import com.wonder.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @RequestMapping
  public @ResponseBody Iterable<User> getAllUsers() {
    return userRepository.findAll();
  }
}
