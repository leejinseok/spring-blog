package com.wonder.blog.common;

import com.wonder.blog.entity.User;
import com.wonder.blog.security.UserContext;
import com.wonder.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

public class CurrentUser {
  public static UserContext create() {
    return (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
