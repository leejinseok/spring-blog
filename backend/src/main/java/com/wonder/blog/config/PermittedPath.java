package com.wonder.blog.config;

public enum PermittedPath {

  API_ROOT_URL("/api/v1/**"),
  LOGIN_URL("/api/v1/auth/login"),
  REFRESH_TOKEN_URL("/api/v1/auth/token"),
  POSTS_URL("/api/v1/posts"),
  POST_URL("/api/v1/posts/*"),
  SIGNUP_URL("/api/v1/auth/signup");

  final private String value;

  PermittedPath(String value){
    this.value = value;
  }

  public String value() {
    return value;
  }
}
