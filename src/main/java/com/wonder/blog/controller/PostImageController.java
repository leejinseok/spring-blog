package com.wonder.blog.controller;

import com.wonder.blog.service.PostImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/posts/images")
@RequiredArgsConstructor
public class PostImageController {

  private final PostImageService postImageService;

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePostImage(@PathVariable("id") int id) {
    postImageService.deletePostImage(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}