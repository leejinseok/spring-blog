package com.wonder.blog.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.wonder.blog.dto.PostDto;
import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.User;
import com.wonder.blog.exception.CustomException;
import com.wonder.blog.exception.DataNotFoundException;
import com.wonder.blog.security.UserContext;
import com.wonder.blog.service.PostService;
import com.wonder.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/posts")
public class PostController {
  @Autowired
  PostService postService;

  @Autowired
  UserService userService;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<PostDto> addPost(@RequestParam String title, @RequestParam String content) {
    return new ResponseEntity<>(new PostDto(postService.addPost(title, content)), HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<PostDto>> getPosts(Pageable pageable) {
    return new ResponseEntity<>(postService.getPosts(pageable).stream().map(post -> new PostDto(post)).collect(Collectors.toList()), HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  public ResponseEntity<PostDto> getPost(@PathVariable int id) {
    Post post = postService.getPost(id);
    if (post == null) {
      throw new DataNotFoundException("Post id: " + id + " not founded");
    }
    return new ResponseEntity<>(new PostDto(postService.getPost(id)), HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
  public ResponseEntity<PostDto> updatePost(@PathVariable int id, @RequestParam String title, @RequestParam String content) {
    return new ResponseEntity<>(new PostDto(postService.updatePost(id, title, content)), HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
  public int deletePost(@PathVariable int id) throws CustomException {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    UserContext userContext = (UserContext) securityContext.getAuthentication().getPrincipal();

    User user = userService.getByUserEmail(userContext.getEmail());
    postService.deletePost(id, user);

    return id;
  }
}
