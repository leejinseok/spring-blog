package com.wonder.blog.controller;

import com.wonder.blog.dto.PostDto;
import com.wonder.blog.entity.Post;
import com.wonder.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/posts")
public class PostController {
  @Autowired
  PostService postService;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<PostDto> addUser(@RequestParam String title, @RequestParam String text) {
    Post post = postService.addPost(title, text);
    PostDto postDto = new PostDto();
    postDto.setId(post.getId());
    postDto.setTitle(post.getTitle());
    postDto.setText(post.getText());
    return new ResponseEntity<>(postDto, HttpStatus.CREATED);
  }
}
