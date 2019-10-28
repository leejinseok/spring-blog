package com.wonder.blog.common;

import org.springframework.http.HttpMethod;

public class RequestMapping {
  private String url;
  private HttpMethod method;

  public RequestMapping(String url, HttpMethod method) {
    this.url = url;
    this.method = method;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public HttpMethod getMethod() {
    return method;
  }

  public void setMethod(HttpMethod method) {
    this.method = method;
  }
}
