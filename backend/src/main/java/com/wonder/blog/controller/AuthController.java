package com.wonder.blog.controller;

import com.wonder.blog.dto.UserDto;
import com.wonder.blog.entity.User;
import com.wonder.blog.security.UserContext;
import com.wonder.blog.service.UserService;
import com.wonder.blog.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static com.wonder.blog.util.JwtUtil.JWT_TOKEN_NAME;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {
  private final UserService userService;
  private final CookieUtil cookieUtil;

  @Autowired
  public AuthController(UserService userService, CookieUtil cookieUtil) {
    this.userService = userService;
    this.cookieUtil = cookieUtil;
  }

  @PostMapping("/signup")
  public ResponseEntity<UserDto> addUser(@RequestBody User user) {
    return new ResponseEntity<>(new UserDto(userService.addUser(user)), HttpStatus.CREATED);
  }

  @GetMapping("/session")
  public ResponseEntity<UserContext> session() {
    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return new ResponseEntity<>(userContext, HttpStatus.OK);
  }

  @PostMapping("/logout")
  public ResponseEntity<UserContext> logout(HttpServletResponse response) {
    UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    cookieUtil.clear(response, JWT_TOKEN_NAME);
    return new ResponseEntity<>(userContext, HttpStatus.OK);
  }
}
