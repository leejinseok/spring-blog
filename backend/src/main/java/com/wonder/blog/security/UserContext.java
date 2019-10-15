package com.wonder.blog.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class UserContext {
  private final String email;
  private final List<GrantedAuthority> authorities;

  private UserContext(String email, List<GrantedAuthority> authorities) {
    this.email = email;
    this.authorities = authorities;
  }

  public static UserContext create(String email, List<GrantedAuthority> authorities) {
    return new UserContext(email, authorities);
  }

  public String getEmail() {
    return email;
  }

  public List<GrantedAuthority> getAuthorities() {
    return authorities;
  }
}
