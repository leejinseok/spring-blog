package com.wonder.blog.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.wonder.blog.dto.PostDto;
import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.PostImage;
import com.wonder.blog.entity.User;
import com.wonder.blog.exception.CustomException;
import com.wonder.blog.exception.DataNotFoundException;
import com.wonder.blog.security.UserContext;
import com.wonder.blog.service.PostImageService;
import com.wonder.blog.service.PostService;
import com.wonder.blog.service.UserService;
import com.wonder.blog.util.AwsS3Util;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
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

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<PostDto> addPost(@RequestBody Post post) {
    return new ResponseEntity<>(new PostDto(postService.addPost(post.getTitle(), post.getContent())), HttpStatus.CREATED);
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
  public ResponseEntity<PostDto> updatePost(@PathVariable int id, @RequestBody Post post) {
    return new ResponseEntity<>(new PostDto(postService.updatePost(id, post.getTitle(), post.getContent())), HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.PUT, value ="/{id}/images")
  public PostImage uploadPostImage(@PathVariable int id, @RequestParam("file") MultipartFile file) throws IOException {

    AwsS3Util awsS3Util = new AwsS3Util();
    String key = id + "/" + UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
    awsS3Util.upload(key, file);

    Post post = postService.getPost(id);
    PostImage postImage = new PostImage();
    postImage.setPost(post);
    postImage.setS3_key(key);
    postImage.setCreatedAt(LocalDateTime.now());

    return postImageService.addPostImage(postImage);
  }

  @RequestMapping(method = RequestMethod.DELETE, value ="/{postId}/images/{imageId}")
  public PostImage uploadPostImage(@PathVariable int imageId, @RequestBody PostImage postImage) throws IOException {
    AwsS3Util awsS3Util = new AwsS3Util();
    awsS3Util.delete(postImage.getS3_key());
    postImageService.deletePostImage(imageId);
    return postImage;
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
  public int deletePost(@PathVariable int id) throws CustomException {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    UserContext userContext = (UserContext) securityContext.getAuthentication().getPrincipal();

    Post post = postService.getPost(id);
    Collection<PostImage> postImages = postImageService.getPostImagesByPost(post);

    AwsS3Util awsS3Util = new AwsS3Util();

    for (PostImage postImage : postImages) {
      awsS3Util.delete(postImage.getS3_key());
    }

    User user = userService.getByUserEmail(userContext.getEmail());
    postService.deletePost(id, user);
    return id;
  }
}
