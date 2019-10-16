package com.wonder.blog.dto;

public class PostImageDto {
  private int id;
  private String s3Key;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getS3_key() {
    return s3Key;
  }

  public void setS3_key(String s3Key) {
    this.s3Key = s3Key;
  }
}
