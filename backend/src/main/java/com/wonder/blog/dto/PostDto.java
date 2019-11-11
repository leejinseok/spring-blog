package com.wonder.blog.dto;

import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.PostImage;
import com.wonder.blog.entity.PostTag;
import com.wonder.blog.entity.User;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
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
  private Collection<PostTag> postTags;
  private UserDto user;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public PostDto(Post post) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.content = post.getContent();
    this.postImages = post.getPostImages().stream().map(PostImageDto::new).collect(Collectors.toList());
    this.postTags = post.getPostTags();
    this.user = new UserDto(post.getUser());
    this.createdAt = post.getCreatedAt();
    this.updatedAt = post.getUpdatedAt();
  }

  @Getter @Setter
  public static class RegisterReq {
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    private Collection<PostTag> postTags;
    private MultipartFile file;
  }
}
