package com.wonder.blog.security;

import com.wonder.blog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthProvider implements AuthenticationProvider {

  private final JwtUtil jwtUtil;

  @Autowired
  public JwtAuthProvider(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  public Authentication authenticate(Authentication authentication) {
    String token = (String) authentication.getCredentials();
    UserContext userContext = jwtUtil.decodeToken(token);
    return new JwtAuthToken(userContext, userContext.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (AbstractAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
