package com.wonder.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wonder.blog.common.ApiResponse;
import com.wonder.blog.util.CookieUtil;
import com.wonder.blog.util.JwtUtil;
import jdk.vm.ci.meta.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.wonder.blog.util.JwtUtil.JWT_TOKEN_NAME;

public class AjaxAuthFilter extends AbstractAuthenticationProcessingFilter {

  @Autowired
  JwtUtil jwtUtil;

  public AjaxAuthFilter(String loginUrl, AuthenticationManager authenticationManager) {
    super(loginUrl);
    this.setAuthenticationManager(authenticationManager);
  };

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    String email = request.getParameter("email");
    String password = request.getParameter("password");

    if (email.isEmpty() || password.isEmpty()) {
      throw new AuthenticationServiceException("Email and Password must be provided");
    }

    return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(email, password));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    UserContext userContext = (UserContext) authResult.getPrincipal();

    String token = jwtUtil.generateToken(userContext);
    CookieUtil.create(response, JWT_TOKEN_NAME, token, false, -1, "localhost");
    String json = new GsonBuilder().create().toJson(new ApiResponse(HttpStatus.OK.value(), "success", userContext));
    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(json);
    response.getWriter().flush();
    response.getWriter().close();
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    SecurityContextHolder.clearContext();

    Map<String, Object> map = new HashMap<>();
    map.put("timestamp", LocalDateTime.now().toString());
    map.put("status", HttpStatus.UNAUTHORIZED.value());
    map.put("message", failed.getMessage());

    Gson gson = new GsonBuilder().create();
    String json = gson.toJson(map);

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(json);
    response.getWriter().flush();
    response.getWriter().close();
  }
}
