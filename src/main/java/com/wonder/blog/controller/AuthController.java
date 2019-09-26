package com.wonder.blog.controller;

import com.wonder.blog.dto.UserDto;
import com.wonder.blog.entity.User;
import com.wonder.blog.repository.UserRepository;
import com.wonder.blog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<UserDto> addUser(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
    User user = new User();
    user.setName(name);
    user.setEmail(email);
    user.setPassword(bCryptPasswordEncoder.encode(password));

    user = userRepository.save(user);
    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setEmail(user.getEmail());
    userDto.setName(user.getName());

    return new ResponseEntity<>(userDto, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.POST, path = "/login")
  public ResponseEntity<User> login(@RequestParam String email, @RequestParam String password) throws AuthenticationException {
    User user = authService.login(email, password);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }
}
