package com.wonder.blog.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class DataDuplicateException extends DataIntegrityViolationException {
  public DataDuplicateException(String msg) {
    super(msg);
  }
}
