package com.wonder.blog.controller;

import com.wonder.blog.service.PostImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/posts/images")
@RequiredArgsConstructor
public class PostImageController {
  private final PostImageService postImageService;

  @DeleteMapping("/{id}")
  public int deletePostImage(@PathVariable("id") int id) {
    return postImageService.deletePostImage(id);
  }
}
