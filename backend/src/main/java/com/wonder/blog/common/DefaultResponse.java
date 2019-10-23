package com.wonder.blog.common;

public class DefaultResponse {
  private int status;
  private String message;
  private Object result;

  public DefaultResponse(int status, String message, Object result) {
    this.status = status;
    this.message = message;
    this.result = result;
  }

  public DefaultResponse(int status, String message) {
    this.status = status;
    this.message = message;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getResult() {
    return result;
  }

  public void setResult(Object result) {
    this.result = result;
  }

  @Override
  public String toString() {
    return "DefaultResponse [statusCode=" + status + ", message=" + message +"]";
  }

}
