package com.wonder.blog.dto;

import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.PostImage;
import com.wonder.blog.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class PostDto {
  private Integer id;
  private String title;
  private String content;
  private UserDto userDto;
  private List<PostImage> postImages;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public PostDto() {}

  public List<PostImage> getPostImages() {
    return postImages;
  }

  public void setPostImages(List<PostImage> postImages) {
    this.postImages = postImages;
  }

  public PostDto(Post post) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.content = post.getContent();
    this.userDto = new UserDto(post.getUser());
    this.postImages = post.getPostImages();
    this.createdAt = post.getCreatedAt();
    this.updatedAt = post.getUpdatedAt();
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public UserDto getUserDto() {
    return userDto;
  }

  public void setUserDto(UserDto userDto) {
    this.userDto = userDto;
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
