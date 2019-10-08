package com.wonder.blog.security.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import javax.security.auth.Subject;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
  private String token;

  public JwtAuthenticationToken(String token) {
    super(null);
    this.token = token;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return null;
  }
}
