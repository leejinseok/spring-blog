package com.wonder.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonder.blog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {
  private final ObjectMapper mapper;

  @Autowired
  public AuthenticationSuccessHandler(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    UserContext userContext = (UserContext) authentication.getPrincipal();

    JwtUtil jwtUtil = new JwtUtil();
    String token = jwtUtil.generateToken(userContext);

    mapper.writeValue(response.getWriter(), token);

//    mapper.writeValue(response.getWriter(), ErrorResponse.of("Authentication success", ErrorCode.AUTHENTICATION, HttpStatus.OK));
//    mapper.writeValue(response.getWriter(), LoginResponse.of());
  }
}
