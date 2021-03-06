package com.wonder.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wonder.blog.serializer.UserSerializer;
import jdk.vm.ci.meta.Local;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "posts")
@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
public class Post implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(length = 36)
  private String uuid;

  @Column(length = 50, nullable = false)
  private String title;

  @Column(columnDefinition = "text")
  private String content;

  @CreatedDate
  private LocalDateTime createdAt;

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
    this.uuid = UUID.randomUUID().toString();
    this.createdAt = LocalDateTime.now();
  }

  @PreUpdate
  private void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  public void setPostTags(Collection<PostTag> postTags) {
    // Casecade Persist 위함
    for (PostTag postTag : postTags) {
      postTag.setPost(this);
    }
    this.postTags = postTags;
  }

  public void clearAndAddPostTags(Collection<PostTag> postTags) {
    this.getPostTags().clear();
    for (PostTag postTag : postTags) {
      postTag.setPost(this);
      this.getPostTags().add(postTag);
    }
  }
}
