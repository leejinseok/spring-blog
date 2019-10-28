package com.wonder.blog.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class DefaultResponse {
  private int status;
  private String message;
  private Object result;

  public DefaultResponse(int status, String message) {
    this.status = status;
    this.message = message;
  }

}
