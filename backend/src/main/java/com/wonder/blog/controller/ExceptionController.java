package com.wonder.blog.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wonder.blog.exception.CustomException;
import com.wonder.blog.exception.DataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@ControllerAdvice
public class ExceptionController {

  @ExceptionHandler(CustomException.class)
  public void custom(Exception exception, HttpServletResponse response) throws IOException {
    Map<String, Object> map = new HashMap<>();
    map.put("timestamp", LocalDateTime.now().toString());
    map.put("message", exception.getMessage());
    map.put("error", "permission denied");
    map.put("status", HttpStatus.UNAUTHORIZED.value());


    Gson gson = new GsonBuilder().create();
    String json = gson.toJson(map);

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(json);
  }

  @ExceptionHandler(DataNotFoundException.class)
  public void notFound(Exception exception, HttpServletResponse response) throws IOException {
    Map<String, Object> map = new HashMap<>();
    map.put("timestamp", LocalDateTime.now().toString());
    map.put("message", exception.getMessage());
    map.put("error", "data not found");
    map.put("status", HttpStatus.NOT_FOUND.value());

    Gson gson = new GsonBuilder().create();
    String json = gson.toJson(map);

    response.setStatus(HttpStatus.NOT_FOUND.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(json);
  }
}
