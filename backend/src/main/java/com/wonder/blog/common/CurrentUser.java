package com.wonder.blog.common;

import com.wonder.blog.entity.User;
import com.wonder.blog.security.UserContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUser {
  public static UserContext CurrentUser() {
    return (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
