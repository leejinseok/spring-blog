package com.wonder.blog.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts_images")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class PostImage {

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

  @Column(nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;
}
