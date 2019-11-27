package com.wonder.blog.dto;

import com.wonder.blog.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class UserDto {

  private Integer id;
  private String name;
  private String email;

  public UserDto(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.email = user.getEmail();
  }

  @Getter @Setter
  public static class RegisterReq {
    @NotEmpty
    private String name;
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
  }
}
