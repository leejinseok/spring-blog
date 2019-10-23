package com.wonder.blog.controller;

import com.wonder.blog.service.PostImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/posts/images")
public class PostImageController {
  private final PostImageService postImageService;

  @Autowired
  public PostImageController(PostImageService postImageService) {
    this.postImageService = postImageService;
  }

  @DeleteMapping("/{id}")
  public int deletePostImage(@PathVariable("id") int id) {
    postImageService.deletePostImage(id);
    return id;
  }
}
