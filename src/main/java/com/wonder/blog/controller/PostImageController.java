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

  @DeleteMapping("/{uuid}")
  public ResponseEntity<Void> deletePostImage(@PathVariable("uuid") String uuid) {
    postImageService.deletePostImage(uuid);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
