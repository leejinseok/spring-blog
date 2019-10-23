package com.wonder.blog.service;

import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.PostImage;
import com.wonder.blog.exception.DataNotFoundException;
import com.wonder.blog.repository.PostImageRepository;
import com.wonder.blog.util.AwsS3Util;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostImageService {
  @Autowired
  PostImageRepository postImageRepository;

  @Autowired
  PostService postService;

  public PostImage addPostImage(int postId, MultipartFile file) throws IOException {
    AwsS3Util awsS3Util = new AwsS3Util();
    String key = postId + "/" + UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
    awsS3Util.upload(key, file);

    Post post = postService.getPost(postId);
    PostImage postImage = new PostImage();
    postImage.setPost(post);
    postImage.setS3Key(key);
    postImage.setCreatedAt(LocalDateTime.now());

    return postImageRepository.save(postImage);
  }

  public Collection<PostImage> getPostImagesByPost(Post post) {
    return postImageRepository.findAllByPost(post);
  }

  public Optional<PostImage> getPostImageById(int id) {
    return postImageRepository.findById(id);
  }

  public void deletePostImage(int id) {
    PostImage postImage = getPostImageById(id).orElseThrow(() -> new DataNotFoundException(id + " id postImage not found"));
    AwsS3Util awsS3Util = new AwsS3Util();
    awsS3Util.delete(postImage.getS3Key());
    postImageRepository.deleteById(postImage.getId());
  }
}
