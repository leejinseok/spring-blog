package com.wonder.blog.exception;

public class AccessNotOwnedResourceException extends RuntimeException {
  public AccessNotOwnedResourceException(String msg) {
    super(msg);
  }
}
