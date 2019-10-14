package com.wonder.blog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonder.blog.security.*;
import com.wonder.blog.security.ajax.AjaxAuthenticationProvider;
import com.wonder.blog.security.ajax.AjaxLoginProcessingFilter;
import com.wonder.blog.security.ajax.AjaxAuthenticationFailureHandler;
import com.wonder.blog.security.ajax.AjaxAuthenticationSuccessHandler;
import com.wonder.blog.security.jwt.JwtAuthenticationFailureHandler;
import com.wonder.blog.security.jwt.JwtAuthenticationProcessiongFilter;
import com.wonder.blog.security.jwt.JwtAuthenticationProvider;
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

import java.util.*;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  public static final String API_ROOT_URL = "/api/v1/**";
  public static final String LOGIN_URL = "/api/v1/auth/login";
  public static final String REFRESH_TOKEN_URL = "/api/v1/auth/token";
  public static final String POSTS_URL = "/api/v1/posts";

  @Autowired ObjectMapper objectMapper;
  @Autowired PasswordEncoder passwordEncoder;
  @Autowired AuthenticationManager authenticationManager;

  @Autowired AjaxAuthenticationProvider ajaxAuthenticationProvider;
  @Autowired AjaxAuthenticationFailureHandler ajaxFailureHandler;
  @Autowired AjaxAuthenticationSuccessHandler ajaxSuccessHandler;

  @Autowired JwtAuthenticationProvider jwtAuthenticationProvider;
  @Autowired JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;


  protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter(String loginEntryPoint) {
    AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(loginEntryPoint, ajaxSuccessHandler, ajaxFailureHandler, objectMapper);
    filter.setAuthenticationManager(this.authenticationManager);
    return filter;
  }

//  protected JwtAuthenticationProcessiongFilter buildJwtAuthenticationFilter(List<String> pathsToSkip, String pattern) throws Exception {
//    SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
//    JwtAuthenticationProcessiongFilter filter = new JwtAuthenticationProcessiongFilter(jwtAuthenticationFailureHandler, matcher);
//    filter.setAuthenticationManager(this.authenticationManager);
//    return filter;
//  }

  protected JwtAuthenticationProcessiongFilter buildJwtAuthenticationFilter(Map<String, HttpMethod> pathsToSkip, String pattern) throws Exception {
    SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
    JwtAuthenticationProcessiongFilter filter = new JwtAuthenticationProcessiongFilter(jwtAuthenticationFailureHandler, matcher);
    filter.setAuthenticationManager(this.authenticationManager);
    return filter;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
//    List<String> permitAllEndpointList = Arrays.asList(
//      LOGIN_URL,
//      REFRESH_TOKEN_URL,
//      "/console"
//    );

    Map<String, HttpMethod> permitAllMap = new HashMap<>();
    permitAllMap.put(LOGIN_URL, HttpMethod.POST);
    permitAllMap.put(REFRESH_TOKEN_URL, HttpMethod.PATCH);
    permitAllMap.put(POSTS_URL, HttpMethod.GET);

    http.csrf().disable()
      .exceptionHandling()
      .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

      .and()
        .authorizeRequests()
//        .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()]))
        .antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
        .antMatchers(HttpMethod.PATCH, REFRESH_TOKEN_URL).permitAll()
        .antMatchers(HttpMethod.GET, POSTS_URL).permitAll()
        .antMatchers("/console").permitAll()

      .and()
        .addFilterBefore(new CustomCorsFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(buildAjaxLoginProcessingFilter(LOGIN_URL), UsernamePasswordAuthenticationFilter.class)
//        .addFilterBefore(buildJwtAuthenticationFilter(permitAllEndpointList, API_ROOT_URL), UsernamePasswordAuthenticationFilter.class);
        .addFilterBefore(buildJwtAuthenticationFilter(permitAllMap, API_ROOT_URL), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(ajaxAuthenticationProvider);
    auth.authenticationProvider(jwtAuthenticationProvider);
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
