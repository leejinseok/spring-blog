package com.wonder.blog.service;

import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.PostImage;
import com.wonder.blog.entity.User;
import com.wonder.blog.exception.CustomException;
import com.wonder.blog.exception.DataNotFoundException;
import com.wonder.blog.repository.PostRepository;
import com.wonder.blog.security.UserContext;
import com.wonder.blog.util.AwsS3Util;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class PostService {
  private final PostRepository postRepository;
  private final UserService userService;
  private final PostImageService postImageService;

  @Autowired
  public PostService(PostRepository postRepository, UserService userService, @Lazy PostImageService postImageService) {
    this.postRepository = postRepository;
    this.userService = userService;
    this.postImageService = postImageService;
  }

  public Post addPost(Post post) {
    SecurityContext context = SecurityContextHolder.getContext();
    UserContext userContext = (UserContext) context.getAuthentication().getPrincipal();
    User user = userService.getUserByEmail(userContext.getEmail());
    post.setUser(user);
    post.setCreatedAt(LocalDateTime.now());
    post.setUpdatedAt(LocalDateTime.now());

    return postRepository.save(post);
  }

  public Page<Post> getPosts(Pageable pageable) {
    return postRepository.findAll(pageable);
  }

  public Post getPost(int id) {
    return postRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Post id: " + id + " not founded"));
  }

  public Post updatePost(int id, Post post) {
    post.setId(id);
    post.setUpdatedAt(LocalDateTime.now());
    return postRepository.save(post);
  }

  public void deletePost(int id) {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    UserContext userContext = (UserContext) securityContext.getAuthentication().getPrincipal();
    User user = userService.getUserByEmail(userContext.getEmail());

    Post post = getPost(id);
    if (!post.getUser().getId().equals(user.getId())) {
      throw new CustomException("This post not your own");
    }

    Collection<PostImage> postImages = postImageService.getPostImagesByPost(post);

    AwsS3Util awsS3Util = new AwsS3Util();
    for (PostImage postImage : postImages) {
      awsS3Util.delete(postImage.getS3Key());
    }

    postRepository.deleteById(id);
  }
}
