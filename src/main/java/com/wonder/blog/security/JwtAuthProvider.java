package com.wonder.blog.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
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

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    System.out.println("JWT PROVIDER");
    return null;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (AbstractAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
