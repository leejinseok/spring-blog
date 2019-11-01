package com.wonder.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "posts")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(length = 50, nullable = false)
  private String title;

  @Column(columnDefinition = "text")
  private String content;

  @ManyToOne
  @JoinColumn()
  private User user;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "post_id")
  @JsonIgnore
  private Collection<PostImage> postImages = new ArrayList<>();

  @Column(nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;
}
