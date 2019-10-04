package com.wonder.blog.config;

import com.wonder.blog.security.AuthenticationFailureHandler;
import com.wonder.blog.security.AuthenticationSuccessHandler;
import com.wonder.blog.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  LoginService loginService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  AuthenticationFailureHandler authenticationFailureHandler;

  @Autowired
  AuthenticationSuccessHandler authenticationSuccessHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.authorizeRequests()
        .antMatchers("**").permitAll()
            .and()
            .formLogin()
            .loginProcessingUrl("/api/v1/auth/login")
            .usernameParameter("email")
            .passwordParameter("password")
            .failureHandler(authenticationFailureHandler)
            .successHandler(authenticationSuccessHandler);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.eraseCredentials(false).userDetailsService(loginService).passwordEncoder(passwordEncoder);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
