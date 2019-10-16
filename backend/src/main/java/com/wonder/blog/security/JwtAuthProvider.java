package com.wonder.blog.security;

import com.wonder.blog.exception.JwtExpiredException;
import com.wonder.blog.exception.TokenIsNullException;
import com.wonder.blog.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthProvider implements AuthenticationProvider {
  @Autowired
  JwtUtil jwtUtil;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String token = (String) authentication.getCredentials();
    if (token == null) {
      throw new TokenIsNullException("Token is not provided");
    }

    try {
      UserContext userContext = jwtUtil.decodeToken(token);
      return new JwtAuthToken(userContext, userContext.getAuthorities());
    } catch (ExpiredJwtException e) {
      throw new ExpiredJwtException(e.getHeader(), e.getClaims(), e.getMessage());
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (AbstractAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
