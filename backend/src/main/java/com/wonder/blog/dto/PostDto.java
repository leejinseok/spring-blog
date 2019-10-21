package com.wonder.blog.dto;

import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.PostImage;
import com.wonder.blog.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PostDto {
  private Integer id;
  private String title;
  private String content;
  private Collection<PostImageDto> postImages;
  private UserDto user;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public PostDto() {}

  public PostDto(Post post) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.content = post.getContent();
    if (post.getPostImages() != null) {
      this.postImages = post.getPostImages().stream().map(item -> new PostImageDto(item)).collect(Collectors.toList());
    }
    this.user = new UserDto(post.getUser());
    this.createdAt = post.getCreatedAt();
    this.updatedAt = post.getUpdatedAt();
  }

  public UserDto getUser() {
    return user;
  }

  public void setUser(UserDto user) {
    this.user = user;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Collection<PostImageDto> getPostImages() {
    return postImages;
  }

  public void setPostImages(Collection<PostImageDto> postImages) {
    this.postImages = postImages;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
