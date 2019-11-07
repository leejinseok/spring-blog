package com.wonder.blog.controller;

import com.wonder.blog.dto.PageDto;
import com.wonder.blog.dto.PostDto;
import com.wonder.blog.dto.PostImageDto;
import com.wonder.blog.entity.Post;
import com.wonder.blog.exception.CustomException;
import com.wonder.blog.service.PostImageService;
import com.wonder.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
  private final PostService postService;
  private final PostImageService postImageService;

  @PostMapping
  public ResponseEntity<PostDto> addPost(@ModelAttribute @Valid PostDto.RegisterReq dto) throws IOException {
    return new ResponseEntity<>(new PostDto(postService.addPost(dto)), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<PageDto> getPosts(Pageable pageable) {
    return new ResponseEntity<>(new PageDto(postService.getPosts(pageable)), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostDto> getPost(@PathVariable int id) {
    return new ResponseEntity<>(new PostDto(postService.getPost(id)), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PostDto> updatePost(@PathVariable int id, @RequestBody Post post) {
    return new ResponseEntity<>(new PostDto(postService.updatePost(id, post)), HttpStatus.OK);
  }

  @PutMapping("/{id}/images")
  public ResponseEntity<PostImageDto> uploadPostImage(@PathVariable int id, @RequestParam("file") MultipartFile file) throws IOException {
    return new ResponseEntity<>(new PostImageDto(postImageService.addPostImage(id, file)), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePost(@PathVariable int id) throws CustomException {
    postService.deletePost(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
