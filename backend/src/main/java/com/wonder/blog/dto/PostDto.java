package com.wonder.blog.dto;

import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.PostImage;
import com.wonder.blog.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class PostDto {

  private Integer id;
  private String title;
  private String content;
  private Collection<PostImageDto> postImages;
  private UserDto user;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public PostDto(Post post) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.content = post.getContent();
    this.postImages = post.getPostImages().stream().map(PostImageDto::new).collect(Collectors.toList());
    this.user = new UserDto(post.getUser());
    this.createdAt = post.getCreatedAt();
    this.updatedAt = post.getUpdatedAt();
  }
}
