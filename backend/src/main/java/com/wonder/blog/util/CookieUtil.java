package com.wonder.blog.util;

import com.wonder.blog.config.AppProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CookieUtil {

  private final AppProperties appProperties;

  public CookieUtil(AppProperties appProperties) {
    this.appProperties = appProperties;
  }

  public void create(HttpServletResponse httpServletResponse, String name, String value, Boolean secure, Integer maxAge, String domain) {
    Cookie cookie = new Cookie(name, value);
    cookie.setSecure(secure);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(maxAge);
    cookie.setDomain(domain);
    cookie.setPath("/");
    httpServletResponse.addCookie(cookie);
  }

  public void clear(HttpServletResponse httpServletResponse, String name) {
    Cookie cookie = new Cookie(name, null);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge(0);
    cookie.setDomain(appProperties.getBaseUrl());
    httpServletResponse.addCookie(cookie);
  }

  public String getValue(HttpServletRequest httpServletRequest, String name) {
    Cookie cookie = WebUtils.getCookie(httpServletRequest, name);
    return cookie != null ? cookie.getValue() : null;
  }
}

