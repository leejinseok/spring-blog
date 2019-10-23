package com.wonder.blog.security;

import com.google.gson.GsonBuilder;
import com.wonder.blog.common.DefaultResponse;
import com.wonder.blog.entity.User;
import com.wonder.blog.service.UserService;
import com.wonder.blog.util.CookieUtil;
import com.wonder.blog.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.wonder.blog.util.JwtUtil.JWT_TOKEN_NAME;

public class AjaxAuthFilter extends AbstractAuthenticationProcessingFilter {
  private JwtUtil jwtUtil;
  private UserService userService;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public AjaxAuthFilter(String loginUrl, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
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

    User user = userService.getUserByEmail(email);

    if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
      throw new BadCredentialsException("Authentication Failed. Email or Password not valid");
    }

    return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(email, password));

  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    UserContext userContext = (UserContext) authResult.getPrincipal();
    String token = jwtUtil.generateToken(userContext);
    CookieUtil.create(response, JWT_TOKEN_NAME, token, false, -1, "localhost");
    String json = new GsonBuilder().create().toJson(new DefaultResponse(HttpStatus.OK.value(), "success", userContext));
    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(json);
    response.getWriter().flush();
    response.getWriter().close();
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    SecurityContextHolder.clearContext();
    String json = new GsonBuilder().create().toJson(new DefaultResponse(HttpStatus.UNAUTHORIZED.value(), failed.getMessage()));
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(json);
    response.getWriter().flush();
    response.getWriter().close();
  }

  private void setJwtUtil(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  private void setUserService(UserService userService) {
    this.userService = userService;
  }

  private void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }
}
