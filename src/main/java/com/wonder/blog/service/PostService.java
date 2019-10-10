package com.wonder.blog.service;

import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.User;
import com.wonder.blog.repository.PostRepository;
import com.wonder.blog.security.UserContext;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.time.LocalDateTime;
import java.util.List;

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
    post.setContent(text);
    post.setUser(user);
    post.setCreatedAt(LocalDateTime.now());

    return postRepository.save(post);
  }

  public List<Post> getPosts(Integer cursor, Integer offset) {
    Pageable pageable = new Pageable() {
      @Override
      public int getNumberOfPages() {
        return cursor;
      }

      @Override
      public PageFormat getPageFormat(int i) throws IndexOutOfBoundsException {
        return null;
      }

      @Override
      public Printable getPrintable(int i) throws IndexOutOfBoundsException {
        return null;
      }
    };

    return postRepository.findAll();
  }
}
