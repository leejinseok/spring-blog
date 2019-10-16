package com.wonder.blog.dto;

import com.wonder.blog.entity.PostImage;

public class PostImageDto {
  private int id;
  private String s3Key;
  private String url;
  private static final String BASE_URL = "https://leejinseok-blog.s3.ap-northeast-2.amazonaws.com/";

  public PostImageDto(PostImage postImage) {
    this.id = postImage.getId();
    this.s3Key = postImage.getS3Key();
    this.url = BASE_URL + postImage.getS3Key();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getS3Key() {
    return s3Key;
  }

  public void setS3Key(String s3Key) {
    this.s3Key = s3Key;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public static String getBaseUrl() {
    return BASE_URL;
  }
}
