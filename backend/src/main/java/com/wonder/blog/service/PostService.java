package com.wonder.blog.service;

import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.PostImage;
import com.wonder.blog.entity.User;
import com.wonder.blog.exception.CustomException;
import com.wonder.blog.exception.DataNotFoundException;
import com.wonder.blog.repository.PostRepository;
import com.wonder.blog.security.UserContext;
import com.wonder.blog.util.AwsS3Util;
import org.springframework.beans.factory.annotation.Autowired;
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
  @Autowired
  EntityManager entityManager;

  @Autowired
  PostRepository postRepository;

  @Autowired
  UserService userService;

  @Autowired
  PostImageService postImageService;

  public Post addPost(Post post) {
    SecurityContext context = SecurityContextHolder.getContext();
    UserContext userContext = (UserContext) context.getAuthentication().getPrincipal();
    Optional<User> user = userService.getByUserEmail(userContext.getEmail());
    user.ifPresent(post::setUser);
    post.setCreatedAt(LocalDateTime.now());
    post.setUpdatedAt(LocalDateTime.now());

    return postRepository.save(post);
  }

  public Page<Post> getPosts(Pageable pageable) {
    return postRepository.findAll(pageable);
  }

  public Post getPost(int id) {
    Optional<Post> post = postRepository.findById(id);
    if (!post.isPresent()) {
      throw new DataNotFoundException("Post id: " + id + " not founded");
    }

    return post.get();
  }

  public Post updatePost(int id, String title, String content) {
    Post post = getPost(id);
    post.setTitle(title);
    post.setContent(content);
    post.setUpdatedAt(LocalDateTime.now());
    return postRepository.save(post);
  }

  public void deletePost(int id) {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    UserContext userContext = (UserContext) securityContext.getAuthentication().getPrincipal();

    String email = userContext.getEmail();
    User user = userService.getByUserEmail(userContext.getEmail())
      .orElseThrow(() -> new DataNotFoundException(email + " is not founded"));

    Post post = getPost(id);
    if (post.getUser() == null || post.getUser().getId().equals(user.getId())) {
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
