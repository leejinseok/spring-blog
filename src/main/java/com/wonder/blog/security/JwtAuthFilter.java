package com.wonder.blog.security;

import com.wonder.blog.util.CookieUtil;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wonder.blog.config.SecurityConfig.*;
import static com.wonder.blog.security.AjaxAuthFilter.jwtTokenCookieName;

public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {

  public JwtAuthFilter(SkipPathRequestMatcher matcher) {
    super(matcher);
  };

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    System.out.println("JWT 시도");
    String accessToken = CookieUtil.getValue(request, jwtTokenCookieName);
    System.out.println(accessToken);
    return null;
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    super.unsuccessfulAuthentication(request, response, failed);
  }
}
