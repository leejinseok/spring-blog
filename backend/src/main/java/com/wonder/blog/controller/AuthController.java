package com.wonder.blog.controller;

import com.wonder.blog.dto.UserDto;
import com.wonder.blog.security.UserContext;
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

  @PostMapping("/signup")
  public ResponseEntity<UserDto> addUser(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
    return new ResponseEntity<>(new UserDto(userService.addUser(name, email, password)), HttpStatus.CREATED);
  }

  @GetMapping("/session")
  public ResponseEntity<UserContext> session() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    UserContext userContext = (UserContext) securityContext.getAuthentication().getPrincipal();
    return new ResponseEntity<>(userContext, HttpStatus.OK);
  }

  @PostMapping("/logout")
  public ResponseEntity<UserContext> logout(HttpServletResponse response) {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    UserContext userContext = (UserContext) securityContext.getAuthentication().getPrincipal();
    CookieUtil.clear(response, JWT_TOKEN_NAME);
    return new ResponseEntity<>(userContext, HttpStatus.OK);
  }
}
