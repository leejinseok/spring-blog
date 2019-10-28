package com.wonder.blog.security;

import com.wonder.blog.common.RequestMapping;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class SkipPathRequestMatcher implements RequestMatcher {

  private OrRequestMatcher matchers;
  private RequestMatcher processiongMatcher;

  public SkipPathRequestMatcher(List<RequestMapping> pathsToSkip, String processingPath) {
    List<RequestMatcher> requestMatchers = new ArrayList<>();

    pathsToSkip.forEach(e -> {
      AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher(e.getUrl(), e.getMethod().name());
      requestMatchers.add(antPathRequestMatcher);
    });

    matchers = new OrRequestMatcher(requestMatchers);
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
