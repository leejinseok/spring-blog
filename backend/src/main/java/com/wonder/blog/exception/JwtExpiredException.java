package com.wonder.blog.exception;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;

import javax.naming.AuthenticationException;

public class JwtExpiredException extends AuthenticationException {
  public JwtExpiredException(String msg) {
    super(msg);
  }
}
