package com.wonder.blog.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter @Setter
public class UserContext {
  private final String email;
  private final List<GrantedAuthority> authorities;

  public UserContext(String email, List<GrantedAuthority> authorities) {
    this.email = email;
    this.authorities = authorities;
  }

  public static UserContext create(String email, List<GrantedAuthority> authorities) {
    return new UserContext(email, authorities);
  }
}
