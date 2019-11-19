package com.wonder.blog.dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wonder.blog.common.CurrentUser;
import com.wonder.blog.domain.Post;
import com.wonder.blog.domain.PostTag;
import com.wonder.blog.domain.User;
import com.wonder.blog.security.UserContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
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
    private String postTags;
    private User user;
    private MultipartFile file;

    public Collection<PostTag> getPostTags() {
      Collection<PostTag> postTags = new ArrayList<>();
      for (JsonElement postTag : JsonParser.parseString(this.postTags).getAsJsonArray()) {
        String text = postTag.getAsJsonObject().get("text").toString().replace("\"", "");
        postTags.add(PostTag.builder().text(text).build());
      }

      return postTags;
    }
  }

  @Getter @Setter
  public static class UpdateReq {
    @NotEmpty
    private int id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    private Collection<PostTag> postTags = new ArrayList<>();

    public void setPostTags(Collection<PostTag> postTags) {
      this.postTags = postTags.stream().map(e -> PostTag.builder().text(e.getText()).build()).collect(Collectors.toList());
    }
  }
}
