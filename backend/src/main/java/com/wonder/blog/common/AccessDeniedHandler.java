package com.wonder.blog.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    ApiResponse apiResponse = new ApiResponse(403, "Access Denied");
    OutputStream out = response.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(out, apiResponse);
    out.flush();
  }
}
