package com.wonder.blog.dto;

import com.wonder.blog.entity.PostImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class PostImageDto {

  private final String BASE_URL = "https://leejinseok-blog.s3.ap-northeast-2.amazonaws.com/";
  private int id;
  private String s3Key;
  private String url;

  public PostImageDto(PostImage postImage) {
    this.id = postImage.getId();
    this.s3Key = postImage.getS3Key();
    this.url = BASE_URL + postImage.getS3Key();
  }
}
