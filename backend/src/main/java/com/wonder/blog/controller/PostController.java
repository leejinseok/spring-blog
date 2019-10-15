package com.wonder.blog.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.wonder.blog.dto.PostDto;
import com.wonder.blog.entity.Post;
import com.wonder.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/posts")
public class PostController {
  @Autowired
  PostService postService;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<PostDto> addPost(@RequestParam String title, @RequestParam String content) {
    Post post = postService.addPost(title, content);
    PostDto postDto = new PostDto();
    postDto.setId(post.getId());
    postDto.setTitle(post.getTitle());
    postDto.setContent(post.getContent());
    return new ResponseEntity<>(postDto, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<PostDto>> getPosts(Pageable pageable) {
    return new ResponseEntity<>(postService.getPosts(pageable).stream().map(post -> new PostDto(post)).collect(Collectors.toList()), HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  public ResponseEntity<PostDto> getPost(@PathVariable int id) {
    return new ResponseEntity<>(new PostDto(postService.getPost(id)), HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
  public ResponseEntity<PostDto> updatePost(@PathVariable int id, @RequestParam String title, @RequestParam String content) {
    return new ResponseEntity<>(new PostDto(postService.updatePost(id, title, content)), HttpStatus.OK);
  }
}
