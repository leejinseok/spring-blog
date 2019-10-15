package com.wonder.blog.security;

import com.wonder.blog.util.CookieUtil;
import com.wonder.blog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import java.security.AuthProvider;


@Component
public class JwtAuthProvider implements AuthenticationProvider {
  @Autowired
  JwtUtil jwtUtil;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String token = (String) authentication.getCredentials();
    try {
      UserContext userContext = jwtUtil.decodeToken(token);
      return new JwtAuthToken(userContext, userContext.getAuthorities());
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (AbstractAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
