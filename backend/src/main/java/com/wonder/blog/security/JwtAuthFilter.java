package com.wonder.blog.security;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wonder.blog.common.ApiResponse;
import com.wonder.blog.util.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.wonder.blog.util.JwtUtil.JWT_TOKEN_NAME;


public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {

  public JwtAuthFilter(SkipPathRequestMatcher matcher, AuthenticationManager authenticationManager) {
    super(matcher);
    this.setAuthenticationManager(authenticationManager);
  };

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    String token = CookieUtil.getValue(request, JWT_TOKEN_NAME);
    if (token == null || token == "" ) {
      throw new AuthenticationServiceException("token is not provided");
    }

    try {
      return this.getAuthenticationManager().authenticate(new JwtAuthToken(token));
    } catch (ExpiredJwtException e) {
      throw new AuthenticationServiceException(e.getMessage());
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authResult);
    SecurityContextHolder.setContext(context);
    chain.doFilter(request, response);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
    SecurityContextHolder.clearContext();

//    Map<String, Object> map = new HashMap<>();
//    map.put("timestamp", LocalDateTime.now().toString());
//    map.put("status", HttpStatus.UNAUTHORIZED.value());
//    map.put("message", failed.getMessage());

//    ApiResponse apiResponse = new ApiResponse(403, failed.getMessage());

    String json = new GsonBuilder().create().toJson(new ApiResponse(403, failed.getMessage()));
//    String json = gson.toJson(apiResponse);

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(json);
    response.getWriter().flush();
    response.getWriter().close();
  }
}
