package com.wonder.blog.service;

import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.User;
import com.wonder.blog.repository.PostRepository;
import com.wonder.blog.security.UserContext;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PostService {
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
    post.setText(text);
    post.setUser(user);

    return postRepository.save(post);
  }
}
