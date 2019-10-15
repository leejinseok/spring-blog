package com.wonder.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonder.blog.util.CookieUtil;
import com.wonder.blog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

public class AjaxAuthFilter extends AbstractAuthenticationProcessingFilter {
  public static final String JWT_TOKEN_NAME = "JWT-TOKEN";

  public AjaxAuthFilter(String loginUrl, AuthenticationManager authenticationManager) {
    super(loginUrl);
    this.setAuthenticationManager(authenticationManager);
  };

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    String email = request.getParameter("email");
    String password = request.getParameter("password");

    if (email.isEmpty() || password.isEmpty()) {
      throw new AuthenticationServiceException("Email and Password must provided");
    }

    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
    return this.getAuthenticationManager().authenticate(token);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    UserContext userContext = (UserContext) authResult.getPrincipal();

    JwtUtil jwtUtil = new JwtUtil();
    String token = jwtUtil.generateToken(userContext);

    CookieUtil.create(response, JWT_TOKEN_NAME, token, false, -1, "localhost");
    response.getWriter().write(token);
    response.getWriter().flush();
    response.getWriter().close();
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    SecurityContextHolder.clearContext();
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(failed.getMessage());
    response.getWriter().flush();
    response.getWriter().close();
  }
}
