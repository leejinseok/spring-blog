package com.wonder.blog.service;

import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.PostImage;
import com.wonder.blog.repository.PostImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PostImageService {

  @Autowired
  PostImageRepository postImageRepository;

  public PostImage addPostImage(PostImage postImage) {
    return postImageRepository.save(postImage);
  }

  public Collection<PostImage> getPostImagesByPost(Post post) {
    return postImageRepository.findAllByPost(post);
  }

  public void deletePostImage(int id) {
    postImageRepository.deleteById(id);
  }
}
