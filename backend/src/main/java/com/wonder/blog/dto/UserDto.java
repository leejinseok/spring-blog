package com.wonder.blog.dto;

import com.wonder.blog.entity.User;
import lombok.Data;

@Data
public class UserDto {
  private Integer id;
  private String name;
  private String email;

  public UserDto() {}

  public UserDto(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.email = user.getEmail();
  }
}
