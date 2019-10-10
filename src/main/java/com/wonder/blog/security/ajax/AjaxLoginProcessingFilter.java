package com.wonder.blog.security.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonder.blog.security.LoginRequest;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
  @Autowired
  AjaxAuthenticationProvider ajaxAuthenticationProvider;
  private final AjaxAuthenticationSuccessHandler successHandler;
  private final AjaxAuthenticationFailureHandler failureHandler;

  private final ObjectMapper objectMapper;

  public AjaxLoginProcessingFilter(String defaultProcessUrl, AjaxAuthenticationSuccessHandler successHandler, AjaxAuthenticationFailureHandler failureHandler, ObjectMapper objectMapper) {
    super(defaultProcessUrl);
    this.successHandler = successHandler;
    this.failureHandler = failureHandler;
    this.objectMapper = objectMapper;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    if (!HttpMethod.POST.name().equals(request.getMethod())) {
      if(logger.isDebugEnabled()) {
        logger.debug("Authentication method not supported. Request method: " + request.getMethod());
      }

      throw new AuthenticationServiceException("Authentication method not supported");
    }

    String email = request.getParameter("email");
    String password = request.getParameter("password");
    LoginRequest loginRequest = new LoginRequest(email, password);

    if (StringUtils.isBlank(loginRequest.getEmail()) || StringUtils.isBlank(loginRequest.getPassword())) {
      throw new AuthenticationServiceException("email or password not provided");
    }

    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
    return this.getAuthenticationManager().authenticate(token);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    successHandler.onAuthenticationSuccess(request, response, authResult);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    failureHandler.onAuthenticationFailure(request, response, failed);
  }
}
