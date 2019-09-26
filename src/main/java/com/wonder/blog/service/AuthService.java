package com.wonder.blog.service;

import com.wonder.blog.entity.User;
import com.wonder.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public class AuthService {
  @Autowired
  UserRepository userRepository;

  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  public User login(String email, String password) throws AuthenticationException {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new BadCredentialsException("no exist user");
    }

    if (!bCryptPasswordEncoder.matches(password,user.getPassword())) {
      throw new BadCredentialsException("password not matched");
    }

    return user;
  }
}
