package com.wonder.blog.service;

import com.wonder.blog.entity.Post;
import com.wonder.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
  @Autowired
  PostRepository postRepository;

  public Post addPost(String title, String text) {
    Post post = new Post();
    post.setTitle(title);
    post.setText(text);

    return postRepository.save(post);
  }
}
