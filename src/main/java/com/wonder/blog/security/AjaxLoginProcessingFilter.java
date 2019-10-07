package com.wonder.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonder.blog.util.WebUtil;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
  private final AuthenticationSuccessHandler successHandler;
  private final AuthenticationFailureHandler failureHandler;

  private final ObjectMapper objectMapper;

  public AjaxLoginProcessingFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler, ObjectMapper objectMapper) {
    super(defaultProcessUrl);
    this.successHandler = successHandler;
    this.failureHandler = failureHandler;
    this.objectMapper = objectMapper;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    if (!HttpMethod.POST.name().equals(request.getMethod()) || !WebUtil.isAjax(request)) {
      if(logger.isDebugEnabled()) {
        logger.debug("Authentication method not supported. Request method: " + request.getMethod());
      }

      throw new AuthenticationServiceException("Authentication method not supported");
    }

    LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);

    if (StringUtils.isBlank(loginRequest.getEmail()) || StringUtils.isBlank(loginRequest.getPassword())) {
      throw new AuthenticationServiceException("email or Password not provided");
    }

    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
    return this.getAuthenticationManager().authenticate(token);
  }
}
