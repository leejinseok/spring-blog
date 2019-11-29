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
      PostImage postImage = postImageService.uploadAndAddPostImage(post, file);
      post.getPostImages().add(postImage);
    }

    return new ResponseEntity<>(new PostDto(post), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<PageDto> getPosts(Pageable pageable, String q) {
    return new ResponseEntity<>(new PageDto(postService.getPosts(pageable, q)), HttpStatus.OK);
  }

  @GetMapping("/{uuid}")
  public ResponseEntity<PostDto> getPost(@PathVariable String uuid) {
    return new ResponseEntity<>(new PostDto(postService.getPost(uuid)), HttpStatus.OK);
  }

  @PutMapping("/{uuid}")
  public ResponseEntity<PostDto> updatePost(@PathVariable String uuid, @RequestBody @Valid PostDto.UpdateReq dto) {
    return new ResponseEntity<>(new PostDto(postService.updatePost(uuid, dto)), HttpStatus.OK);
  }

  @PutMapping("/{uuid}/images")
  public ResponseEntity<PostImageDto> uploadPostImage(@PathVariable String uuid, @RequestParam("file") MultipartFile file) throws IOException {
    Post post = postService.getPost(uuid);
    PostImage postImage = postImageService.uploadAndAddPostImage(post, file);
    return new ResponseEntity<>(new PostImageDto(postImage), HttpStatus.OK);
  }

  @DeleteMapping("/{uuid}")
  public ResponseEntity<Void> deletePost(@PathVariable String uuid) throws AccessNotOwnedResourceException {
    postImageService.deletePostImageByPost(postService.getPost(uuid));
    postService.deletePost(uuid);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
