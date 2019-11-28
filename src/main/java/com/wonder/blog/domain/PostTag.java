package com.wonder.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_tags")
@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
public class PostTag implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(length = 30, nullable = false)
  private String text;

  @CreatedDate
  private LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "post_id")
  @JsonIgnore
  private Post post;

  @PrePersist
  private void perPersist() {
    this.createdAt = LocalDateTime.now();
  }
}
