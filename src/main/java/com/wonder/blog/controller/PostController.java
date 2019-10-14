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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    postDto.setText(post.getContent());
    return new ResponseEntity<>(postDto, HttpStatus.CREATED);
  }

//  @RequestMapping(method = RequestMethod.GET)
//  public ResponseEntity<List<PostDto>> getPosts(@RequestParam(defaultValue = "0") Integer cursor, @RequestParam(defaultValue = "10") Integer offset) {
//    List<Post> posts = postService.getPosts(cursor, offset);
//    return new ResponseEntity<>(posts.stream().map(post -> new PostDto(post)).collect(Collectors.toList()), HttpStatus.OK);
//  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<PostDto>> getPosts(Pageable pageable) {
    Page<Post> posts = postService.getPosts(pageable);
    return new ResponseEntity<>(posts.stream().map(post -> new PostDto(post)).collect(Collectors.toList()), HttpStatus.OK);
  }
}
