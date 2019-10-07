package com.wonder.blog.security;

import com.wonder.blog.entity.User;
import com.wonder.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  UserService userService;

  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String email = (String) authentication.getPrincipal();
    String password = (String) authentication.getCredentials();

    User user = userService.getByUserEmail(email);


    if (user == null) {
        throw new UsernameNotFoundException("User not found: " + email);
    }

    if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
      throw new BadCredentialsException("Authentication Failed. Email or Password not valid");
    }

    List<GrantedAuthority> authorities = new ArrayList<>();

    UserContext userContext = UserContext.create(user.getEmail(), authorities);
    return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
