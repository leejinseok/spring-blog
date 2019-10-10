package com.wonder.blog.security.jwt;

import com.wonder.blog.security.UserContext;
import com.wonder.blog.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String token = (String) authentication.getCredentials();
    JwtUtil jwtUtil = new JwtUtil();
    UserContext context = jwtUtil.decodeToken(token);
    JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(context, context.getAuthorities());
    return jwtAuthenticationToken;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
