package com.wonder.blog.service;

import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.User;
import com.wonder.blog.repository.PostRepository;
import com.wonder.blog.security.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Service
public class PostService {
  @Autowired
  EntityManager entityManager;

  @Autowired
  PostRepository postRepository;

  @Autowired
  UserService userService;

  public Post addPost(String title, String text) {
    SecurityContext context = SecurityContextHolder.getContext();
    UserContext userContext = (UserContext) context.getAuthentication().getPrincipal();
    User user = userService.getByUserEmail(userContext.getEmail());
    Post post = new Post();
    post.setTitle(title);
    post.setContent(text);
    post.setUser(user);
    post.setCreatedAt(LocalDateTime.now());

    return postRepository.save(post);
  }

  public Page<Post> getPosts(Pageable pageable) {
    return postRepository.findAll(pageable);
  }

  public Post getPost(Integer id) {
    return postRepository.findPostById(id);
  }
}
