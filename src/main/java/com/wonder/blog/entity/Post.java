package com.wonder.blog.entity;

import javax.persistence.*;

@Entity
@Table(name = "posts")
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String title;

  private String text;

  @ManyToOne
  @JoinColumn()
  private User user;
}
