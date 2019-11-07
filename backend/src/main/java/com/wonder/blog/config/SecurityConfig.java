package com.wonder.blog.config;

import com.wonder.blog.common.RequestMapper;
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
    http.authorizeRequests().antMatchers(HttpMethod.GET, MatcherUrl.LOGIN_URL.value()).permitAll();

    http
      .addFilterBefore(ajaxAuthFilter(), UsernamePasswordAuthenticationFilter.class).authenticationProvider(ajaxAuthProvider)
      .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class).authenticationProvider(jwtAuthProvider);
  }

  private AjaxAuthFilter ajaxAuthFilter() {
    AjaxAuthFilter filter = new AjaxAuthFilter(MatcherUrl.LOGIN_URL.value());
    filter.setAuthenticationManager(authenticationManager);
    filter.setJwtUtil(jwtUtil);
    filter.setCookieUtil(cookieUtil);
    filter.setUserService(userService);
    filter.setBCryptPasswordEncoder(bCryptPasswordEncoder());
    filter.setAppProperties(appProperties);
    return filter;
  }

  private JwtAuthFilter jwtAuthFilter() {
    SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip(), MatcherUrl.API_ROOT_URL.value());
    JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(matcher);
    jwtAuthFilter.setAuthenticationManager(authenticationManager);
    jwtAuthFilter.setCookieUtil(cookieUtil);
    return jwtAuthFilter;
  }

  private List<RequestMapper> pathsToSkip() {
    List<RequestMapper> list = new ArrayList<>();
    list.add(new RequestMapper(MatcherUrl.LOGIN_URL.value(), HttpMethod.POST));
    list.add(new RequestMapper(MatcherUrl.REFRESH_TOKEN_URL.value(), HttpMethod.PATCH));
    list.add(new RequestMapper(MatcherUrl.POSTS_URL.value(), HttpMethod.GET));
    list.add(new RequestMapper(MatcherUrl.POST_URL.value(), HttpMethod.GET));
    list.add(new RequestMapper(MatcherUrl.SIGNUP_URL.value(), HttpMethod.POST));
    return list;
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
