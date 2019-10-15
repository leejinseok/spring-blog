package com.wonder.blog.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SkipPathRequestMatcher implements RequestMatcher {
  private OrRequestMatcher matchers;
  private RequestMatcher processiongMatcher;

  public SkipPathRequestMatcher(Map<String, HttpMethod> pathsToSkip, String processingPath) {
    List<RequestMatcher> m = new ArrayList<>();
    for (Map.Entry<String, HttpMethod> entry : pathsToSkip.entrySet()) {
      AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher(entry.getKey(), entry.getValue().name());
      m.add(antPathRequestMatcher);
    }

    matchers = new OrRequestMatcher(m);
    processiongMatcher = new AntPathRequestMatcher(processingPath);
  }

  @Override
  public boolean matches(HttpServletRequest request) {
    if (matchers.matches(request)) { // jwt 토큰 없이 접근이 가능하도록 설정한 path의 경우 jwt 필터 사용 안함
      return false;
    }

    return processiongMatcher.matches(request);
  }
}