package com.wonder.blog.controller;

import com.wonder.blog.dto.UserDto;
import com.wonder.blog.security.UserContext;
import com.wonder.blog.service.AuthService;
import com.wonder.blog.service.UserService;
import com.wonder.blog.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static com.wonder.blog.security.AjaxAuthFilter.JWT_TOKEN_NAME;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {
  @Autowired
  UserService userService;

  @Autowired
  AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<UserDto> addUser(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
    UserDto userDto = new UserDto(userService.addUser(name, email, password));
    return new ResponseEntity<>(userDto, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<UserDto> login(@RequestParam String email, @RequestParam String password) {
    UserDto userDto = new UserDto(authService.login(email, password));
    return new ResponseEntity<>(userDto, HttpStatus.OK);
  }

  @GetMapping("/session")
  public ResponseEntity<UserContext> session() {
    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return new ResponseEntity<>(userContext, HttpStatus.OK);
  }

  @PostMapping("/logout")
  public ResponseEntity<UserContext> logout(HttpServletResponse response) {
    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    CookieUtil.clear(response, JWT_TOKEN_NAME);
    return new ResponseEntity<>(userContext, HttpStatus.OK);
  }
}
