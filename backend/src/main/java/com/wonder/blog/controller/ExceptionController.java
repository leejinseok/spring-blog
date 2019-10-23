package com.wonder.blog.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wonder.blog.common.ErrorResponse;
import com.wonder.blog.exception.CustomException;
import com.wonder.blog.exception.DataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ExceptionController {

  @ExceptionHandler(CustomException.class)
  public void custom(Exception exception, HttpServletResponse response) throws IOException {
    int httpStatus = HttpStatus.UNAUTHORIZED.value();
    String json = new GsonBuilder().create().toJson(new ErrorResponse(httpStatus, exception.getMessage()));
    response.setStatus(httpStatus);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(json);
  }

  @ExceptionHandler(DataNotFoundException.class)
  public void notFound(Exception exception, HttpServletResponse response) throws IOException {
    int httpStatus =  HttpStatus.NOT_FOUND.value();
    String json = new GsonBuilder().create().toJson(new ErrorResponse(httpStatus, exception.getMessage()));
    response.setStatus(httpStatus);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(json);
  }
}
