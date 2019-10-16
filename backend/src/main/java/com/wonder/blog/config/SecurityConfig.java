package com.wonder.blog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonder.blog.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private static final String API_ROOT_URL = "/api/v1/**";
  private static final String LOGIN_URL = "/api/v1/auth/login";
  private static final String LOGOUT_URL = "/api/v1/auth/logout";
  private static final String REFRESH_TOKEN_URL = "/api/v1/auth/token";
  private static final String POSTS_URL = "/api/v1/posts";
  private static final String POST_URL = "/api/v1/posts/*";


  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  AjaxAuthProvider ajaxAuthProvider;

  @Autowired
  JwtAuthProvider jwtAuthProvider;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    Map<String, HttpMethod> permitAllMap = new HashMap<>();
    permitAllMap.put(LOGIN_URL, HttpMethod.POST);
    permitAllMap.put(REFRESH_TOKEN_URL, HttpMethod.PATCH);
    permitAllMap.put(POSTS_URL, HttpMethod.GET);
    permitAllMap.put(POST_URL, HttpMethod.GET);
    permitAllMap.put(LOGOUT_URL, HttpMethod.POST);

    SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(permitAllMap, API_ROOT_URL);
    http.cors().disable();
    http.csrf().disable();
    http.authorizeRequests()
      .antMatchers(LOGIN_URL).permitAll();

    http.addFilterBefore(new AjaxAuthFilter(LOGIN_URL, this.authenticationManager), UsernamePasswordAuthenticationFilter.class)
      .authenticationProvider(ajaxAuthProvider);

    http.addFilterBefore(new JwtAuthFilter(matcher, this.authenticationManager), UsernamePasswordAuthenticationFilter.class)
      .authenticationProvider(jwtAuthProvider);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
