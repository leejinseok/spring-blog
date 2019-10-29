package com.wonder.blog.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class JwtAuthToken extends AbstractAuthenticationToken {
  private String token;
  private UserContext userContext;

  public JwtAuthToken(String token) {
    super(null);
    this.token = token;
  }

  public JwtAuthToken(UserContext userContext, List<GrantedAuthority> authorities) {
    super(authorities);
    this.userContext = userContext;
  }

  @Override
  public Object getCredentials() {
    return this.token;
  }

  @Override
  public Object getPrincipal() {
    return this.userContext;
  }
}
