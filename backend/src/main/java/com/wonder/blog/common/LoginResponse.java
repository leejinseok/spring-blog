package com.wonder.blog.common;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class LoginResponse {
  // HTTP Response Status Code
  private final HttpStatus status;

  // General Error message
  private final String token;

  // Error code
  private final ErrorCode errorCode;

  private final Date timestamp;

  protected LoginResponse(final String token, final ErrorCode errorCode, HttpStatus status) {
    this.token = token;
    this.errorCode = errorCode;
    this.status = status;
    this.timestamp = new java.util.Date();
  }

  public static LoginResponse of(final String token, final ErrorCode errorCode, HttpStatus status) {
    return new LoginResponse(token, errorCode, status);
  }

  public Integer getStatus() {
    return status.value();
  }


  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public Date getTimestamp() {
    return timestamp;
  }
}

