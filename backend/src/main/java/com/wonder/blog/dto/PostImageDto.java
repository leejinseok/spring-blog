package com.wonder.blog.dto;

public class PostImageDto {
  private int id;
  private String s3_key;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getS3_key() {
    return s3_key;
  }

  public void setS3_key(String s3_key) {
    this.s3_key = s3_key;
  }
}
