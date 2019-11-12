package com.wonder.blog.controller;

import com.wonder.blog.common.CurrentUser;
import com.wonder.blog.dto.UserDto;
import com.wonder.blog.domain.User;
import com.wonder.blog.security.UserContext;
import com.wonder.blog.service.UserService;
import com.wonder.blog.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static com.wonder.blog.util.JwtUtil.JWT_TOKEN_NAME;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

  private final UserService userService;
  private final CookieUtil cookieUtil;

  @PostMapping("/signup")
  public ResponseEntity<UserDto> addUser(@RequestBody User user) {
    return new ResponseEntity<>(new UserDto(userService.addUser(user)), HttpStatus.CREATED);
  }

  @GetMapping("/session")
  public ResponseEntity<UserContext> session() {
    UserContext userContext = CurrentUser.create();
    return new ResponseEntity<>(userContext, HttpStatus.OK);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletResponse response) {
    cookieUtil.clear(response, JWT_TOKEN_NAME);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
