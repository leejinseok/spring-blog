package com.wonder.blog.service;

import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.PostImage;
import com.wonder.blog.exception.DataNotFoundException;
import com.wonder.blog.repository.PostImageRepository;
import com.wonder.blog.util.AwsS3Util;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostImageService {

  private final PostImageRepository postImageRepository;
  private final AwsS3Util awsS3Util;

  public PostImage addPostImage(Post post, MultipartFile file) throws IOException {
    String key = awsS3Util.generateS3Key(post.getId(), file.getOriginalFilename());
    awsS3Util.upload(key, file);
    PostImage postImage = PostImage.builder()
      .post(post)
      .s3Key(key)
      .createdAt(LocalDateTime.now())
      .build();

    return postImageRepository.save(postImage);
  }

  @Transactional(readOnly = true)
  public Collection<PostImage> getPostImagesByPost(Post post) {
    return postImageRepository.findAllByPost(post);
  }

  @Transactional(readOnly = true)
  public PostImage getPostImageById(int id) {
    return postImageRepository.findById(id).orElseThrow(() -> new DataNotFoundException(id + " id postImage not found"));
  }

  public void deletePostImage(int id) {
    PostImage postImage = getPostImageById(id);
    awsS3Util.delete(postImage.getS3Key());
    postImageRepository.deleteById(postImage.getId());
  }

  public void deletePostImageByPost(Post post) {
    post.getPostImages().forEach(e -> {
      deletePostImage(e.getId());
    });
  }
}
