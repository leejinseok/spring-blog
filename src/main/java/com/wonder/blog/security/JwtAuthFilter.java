package com.wonder.blog.security;

import com.wonder.blog.util.CookieUtil;
import org.springframework.security.authentication.AuthenticationManager;
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
import static com.wonder.blog.security.AjaxAuthFilter.jwtTokenCookieName;

public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {

  public JwtAuthFilter(SkipPathRequestMatcher matcher, AuthenticationManager authenticationManager) {
    super(matcher);
    this.setAuthenticationManager(authenticationManager);
  };

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    String accessToken = CookieUtil.getValue(request, jwtTokenCookieName);
    return this.getAuthenticationManager().authenticate(new JwtAuthToken(accessToken));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authResult);
    SecurityContextHolder.setContext(context);
    chain.doFilter(request, response);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    super.unsuccessfulAuthentication(request, response, failed);
  }
}
