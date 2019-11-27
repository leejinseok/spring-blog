package com.wonder.blog.controller;

import com.wonder.blog.common.CurrentUser;
import com.wonder.blog.domain.User;
import com.wonder.blog.dto.PageDto;
import com.wonder.blog.dto.PostDto;
import com.wonder.blog.dto.PostImageDto;
import com.wonder.blog.domain.Post;
import com.wonder.blog.domain.PostImage;
import com.wonder.blog.exception.AccessNotOwnedResourceException;
import com.wonder.blog.service.PostImageService;
import com.wonder.blog.service.PostService;
import com.wonder.blog.service.UserService;
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

  private final UserService userService;
  private final PostService postService;
  private final PostImageService postImageService;

  @PostMapping
  public ResponseEntity<PostDto> addPost(@ModelAttribute @Valid PostDto.RegisterReq dto) throws IOException {
    User user = userService.getUserByEmail(CurrentUser.create().getEmail());
    dto.setUser(user);

    Post post = postService.addPost(dto);

    MultipartFile file = dto.getFile();
    if (file != null) {
      post.getPostImages().add(postImageService.addPostImage(post, file));
    }

    return new ResponseEntity<>(new PostDto(post), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<PageDto> getPosts(Pageable pageable, String q) {
    return new ResponseEntity<>(new PageDto(postService.getPosts(pageable, q)), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostDto> getPost(@PathVariable int id) {
    return new ResponseEntity<>(new PostDto(postService.getPost(id)), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PostDto> updatePost(@PathVariable int id, @RequestBody PostDto.UpdateReq dto) {
    return new ResponseEntity<>(new PostDto(postService.updatePost(id, dto)), HttpStatus.OK);
  }

  @PutMapping("/{id}/images")
  public ResponseEntity<PostImageDto> uploadPostImage(@PathVariable int id, @RequestParam("file") MultipartFile file) throws IOException {
    Post post = postService.getPost(id);
    PostImage postImage = postImageService.addPostImage(post, file);
    return new ResponseEntity<>(new PostImageDto(postImage), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePost(@PathVariable int id) throws AccessNotOwnedResourceException {
    postImageService.deletePostImageByPost(postService.getPost(id));
    postService.deletePost(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
