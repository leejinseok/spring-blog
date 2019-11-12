package com.wonder.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wonder.blog.dto.PostDto;
import com.wonder.blog.serializer.UserSerializer;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "posts")
@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(length = 50, nullable = false)
  private String title;

  @Column(columnDefinition = "text")
  private String content;

  @Column(nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdAt;

  @Column
  @LastModifiedDate
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @JsonSerialize(using = UserSerializer.class)
  private User user;

  @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.PERSIST)
  @JsonIgnore
  private Collection<PostImage> postImages = new ArrayList<>();

  @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.PERSIST)
  @JsonIgnore
  private Collection<PostTag> postTags = new ArrayList<>();

  @PrePersist
  private void prePersist() {
    this.createdAt = LocalDateTime.now();
  }

  @PreUpdate
  private void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  public void setPostTags(Collection<PostTag> postTags) {
    // Casecade Persist 위함
    postTags.stream().forEach(e -> e.setPost(this));
    this.postTags = postTags;
  }

  public void clearAndAddPostTags(PostDto.UpdateReq dto) {
    this.postTags.clear();
    dto.getPostTags().forEach(e -> {
      PostTag postTag = PostTag.builder()
        .text(e.getText())
        .post(this)
        .build();
      this.getPostTags().add(postTag);
    });
  }
}
