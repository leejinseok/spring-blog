package com.wonder.blog.security;

import com.wonder.blog.common.DefaultResponse;
import com.wonder.blog.common.ResponseWriter;
import com.wonder.blog.config.AppProperties;
import com.wonder.blog.entity.User;
import com.wonder.blog.exception.CustomException;
import com.wonder.blog.service.UserService;
import com.wonder.blog.util.CookieUtil;
import com.wonder.blog.util.JwtUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.wonder.blog.util.JwtUtil.JWT_TOKEN_NAME;

@Getter @Setter
public class AjaxAuthFilter extends AbstractAuthenticationProcessingFilter {
  private JwtUtil jwtUtil;
  private CookieUtil cookieUtil;
  private UserService userService;
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  private AppProperties appProperties;

  public AjaxAuthFilter(String loginUrl) {
    super(loginUrl);
  };

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    String email = request.getParameter("email");
    String password = request.getParameter("password");

    if (email.isEmpty() || password.isEmpty()) {
      throw new AuthenticationServiceException("Email and Password must be provided");
    }

    User user;
    try {
      user = userService.getUserByEmail(email);
    } catch (UsernameNotFoundException e) {
      throw new BadCredentialsException(e.getMessage());
    }

    if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
      throw new BadCredentialsException("Authentication Failed. Email or Password not valid");
    }

    return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(email, password));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    UserContext userContext = (UserContext) authResult.getPrincipal();
    cookieUtil.create(response, JWT_TOKEN_NAME, jwtUtil.generateToken(userContext), false, -1, appProperties.getBaseUrl());
    ResponseWriter.builder()
      .defaultResponse(new DefaultResponse(HttpStatus.OK.value(), "success", userContext))
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .response(response)
      .build()
      .write();

  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    SecurityContextHolder.clearContext();
    ResponseWriter.builder()
      .defaultResponse(new DefaultResponse(HttpStatus.UNAUTHORIZED.value(), failed.getMessage()))
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .response(response)
      .build()
      .write();
  }
}
