package com.wonder.blog.controller;

import com.wonder.blog.common.ErrorResponse;
import com.wonder.blog.exception.AccessNotOwnedResourceException;
import com.wonder.blog.exception.DataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Advice {

  @ExceptionHandler(AccessNotOwnedResourceException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorResponse custom(Exception exception) {
    return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
  }

  @ExceptionHandler(DataNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse notFound(Exception exception) {
    return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
  }
}