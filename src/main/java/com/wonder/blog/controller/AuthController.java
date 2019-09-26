package com.wonder.blog.controller;

import com.wonder.blog.entity.User;
import com.wonder.blog.repository.UserRepository;
import com.wonder.blog.service.AuthService;
import com.wonder.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {
  @Autowired
  UserRepository userRepository;

  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  AuthService authService;

  @RequestMapping(method = RequestMethod.POST, path = "/signup")
  public @ResponseBody User addUser(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
    User user = new User();
    user.setName(name);
    user.setEmail(email);
    user.setPassword(bCryptPasswordEncoder.encode(password));
    return userRepository.save(user);
  }

  @RequestMapping(method = RequestMethod.POST, path = "/login")
  public ResponseEntity<User> login(@RequestParam String email, @RequestParam String password) throws AuthenticationException  {
    User user = authService.login(email, password);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }
}
