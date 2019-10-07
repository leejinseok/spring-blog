package com.wonder.blog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonder.blog.security.*;
import com.wonder.blog.security.ajax.AjaxAuthenticationProvider;
import com.wonder.blog.security.ajax.AjaxLoginProcessingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  public static final String API_ROOT_URL = "/api/**";

  @Autowired PasswordEncoder passwordEncoder;
  @Autowired AjaxAuthenticationProvider ajaxAuthenticationProvider;
  @Autowired AuthenticationManager authenticationManager;
  @Autowired AuthenticationFailureHandler failureHandler;
  @Autowired AuthenticationSuccessHandler successHandler;
  @Autowired ObjectMapper objectMapper;

  protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter(String loginEntryPoint) {
    AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(loginEntryPoint, successHandler, failureHandler, objectMapper);
    filter.setAuthenticationManager(this.authenticationManager);
    return filter;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
      .exceptionHandling()
      .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

      .and()
        .authorizeRequests()
        .antMatchers("**")
        .permitAll()

      .and()
        .addFilterBefore(new CustomCorsFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(buildAjaxLoginProcessingFilter("/api/v1/auth/login"), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(ajaxAuthenticationProvider);
  }

  //  @Override
  //  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  //    auth.eraseCredentials(false).userDetailsService(loginService).passwordEncoder(passwordEncoder);
  //  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
