package com.wonder.blog.config;

import com.wonder.blog.common.RequestMapping;
import com.wonder.blog.security.*;
import com.wonder.blog.service.UserService;
import com.wonder.blog.util.CookieUtil;
import com.wonder.blog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.*;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String API_ROOT_URL = "/api/v1/**";
  private static final String LOGIN_URL = "/api/v1/auth/login";
  private static final String REFRESH_TOKEN_URL = "/api/v1/auth/token";
  private static final String POSTS_URL = "/api/v1/posts";
  private static final String POST_URL = "/api/v1/posts/*";

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private AjaxAuthProvider ajaxAuthProvider;
  @Autowired
  private JwtAuthProvider jwtAuthProvider;
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private UserService userService;
  @Autowired
  CookieUtil cookieUtil;
  @Autowired
  AppProperties appProperties;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().disable().csrf().disable();
    http.authorizeRequests()
      .antMatchers(LOGIN_URL).permitAll()
      .antMatchers(POSTS_URL).permitAll()
      .antMatchers(POST_URL).permitAll();
    http
      .addFilterBefore(ajaxAuthFilter(),
        UsernamePasswordAuthenticationFilter.class)
      .authenticationProvider(ajaxAuthProvider);

    http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
      .authenticationProvider(jwtAuthProvider);
  }

  private AjaxAuthFilter ajaxAuthFilter() {
    AjaxAuthFilter filter = new AjaxAuthFilter(LOGIN_URL);
    filter.setAuthenticationManager(authenticationManager);
    filter.setJwtUtil(jwtUtil);
    filter.setCookieUtil(cookieUtil);
    filter.setUserService(userService);
    filter.setBCryptPasswordEncoder(bCryptPasswordEncoder());
    filter.setAppProperties(appProperties);
    return filter;
  }

  private JwtAuthFilter jwtAuthFilter() {
    List<RequestMapping> pathsToSkip = getPathsToSkip();
    SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, API_ROOT_URL);

    JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(matcher);
    jwtAuthFilter.setAuthenticationManager(authenticationManager);
    jwtAuthFilter.setCookieUtil(cookieUtil);
    return jwtAuthFilter;
  }

  private List<RequestMapping> getPathsToSkip() {
    List<RequestMapping> pathsToSkip = new ArrayList<>();
    pathsToSkip.add(new RequestMapping(LOGIN_URL, HttpMethod.POST));
    pathsToSkip.add(new RequestMapping(REFRESH_TOKEN_URL, HttpMethod.PATCH));
    pathsToSkip.add(new RequestMapping(POSTS_URL, HttpMethod.GET));
    pathsToSkip.add(new RequestMapping(POST_URL, HttpMethod.GET));
    return pathsToSkip;
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
