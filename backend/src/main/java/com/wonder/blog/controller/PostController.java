package com.wonder.blog.controller;

import com.wonder.blog.dto.PostDto;
import com.wonder.blog.dto.PostImageDto;
import com.wonder.blog.entity.Post;
import com.wonder.blog.exception.CustomException;
import com.wonder.blog.service.PostImageService;
import com.wonder.blog.service.PostService;
import com.wonder.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/posts")
public class PostController {
  @Autowired
  PostService postService;

  @Autowired
  PostImageService postImageService;

  @Autowired
  UserService userService;

  @PostMapping
  public ResponseEntity<PostDto> addPost(@RequestBody Post post) {
    return new ResponseEntity<>(new PostDto(postService.addPost(post.getTitle(), post.getContent())), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<Page<Post>> getPosts(Pageable pageable) {
    Page page = postService.getPosts(pageable);
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

//  @GetMapping
//  public ResponseEntity<List<PostDto>> getPosts(Pageable pageable) {
//    return new ResponseEntity<>(postService.getPosts(pageable).stream().map(post -> new PostDto(post)).collect(Collectors.toList()), HttpStatus.OK);
//  }

  @GetMapping("/{id}")
  public ResponseEntity<PostDto> getPost(@PathVariable int id) {
    return new ResponseEntity<>(new PostDto(postService.getPost(id)), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PostDto> updatePost(@PathVariable int id, @RequestBody Post post) {
    return new ResponseEntity<>(new PostDto(postService.updatePost(id, post.getTitle(), post.getContent())), HttpStatus.OK);
  }

  @PutMapping("/{id}/images")
  public ResponseEntity<PostImageDto> uploadPostImage(@PathVariable int id, @RequestParam("file") MultipartFile file) throws IOException {
    PostImageDto postImageDto = new PostImageDto(postImageService.addPostImage(id, file));
    return new ResponseEntity<>(postImageDto, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Integer> deletePost(@PathVariable int id) throws CustomException {
    postService.deletePost(id);
    return new ResponseEntity<>(id, HttpStatus.OK);
  }
}
