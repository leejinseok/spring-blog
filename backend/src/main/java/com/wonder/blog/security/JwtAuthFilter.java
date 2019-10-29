package com.wonder.blog.security;

import com.google.gson.GsonBuilder;
import com.wonder.blog.common.DefaultResponse;
import com.wonder.blog.util.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Getter;
import lombok.Setter;
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

import static com.wonder.blog.util.JwtUtil.JWT_TOKEN_NAME;

@Getter @Setter
public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {
  private CookieUtil cookieUtil;

  public JwtAuthFilter(SkipPathRequestMatcher matcher) {
    super(matcher);
  };

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    String token = cookieUtil.getValue(request, JWT_TOKEN_NAME);
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
    String json = new GsonBuilder().create().toJson(new DefaultResponse(403, failed.getMessage()));
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(json);
    response.getWriter().flush();
    response.getWriter().close();
  }
}
