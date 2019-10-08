package com.wonder.blog.security.jwt;

import com.wonder.blog.security.UserContext;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
  private String token;
  private UserContext userContext;

  public JwtAuthenticationToken(String token) {
    super(null);
    this.token = token;
  }

  public JwtAuthenticationToken(UserContext userContext, List<GrantedAuthority> authorities) {
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
