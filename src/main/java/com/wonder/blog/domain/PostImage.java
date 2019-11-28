package com.wonder.blog.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_images")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class PostImage implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column
  private String uuid;

  @Column
  private String s3Key;

  @Column
  private String url;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  @PrePersist
  private void prePersiste() {
    this.createdAt = LocalDateTime.now();
  }

  @PreUpdate
  private void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
