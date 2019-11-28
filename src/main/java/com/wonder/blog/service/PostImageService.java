package com.wonder.blog.service;

import com.wonder.blog.domain.Post;
import com.wonder.blog.domain.PostImage;
import com.wonder.blog.exception.DataNotFoundException;
import com.wonder.blog.repository.PostImageRepository;
import com.wonder.blog.util.AwsS3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostImageService {

  private final PostImageRepository postImageRepository;
  private final AwsS3Util awsS3Util;

  public PostImage uploadAndAddPostImage(Post post, MultipartFile file) throws IOException {
    String key = awsS3Util.generateS3Key(post.getId(), file.getOriginalFilename());
    awsS3Util.upload(key, file);

    PostImage postImage = new PostImage();
    postImage.setUuid(UUID.randomUUID().toString());
    postImage.setPost(post);
    postImage.setS3Key(key);

    return postImageRepository.save(postImage);
  }

  public void deletePostImage(String uuid) {
    PostImage postImage = postImageRepository.findOneByUuid(uuid).orElseThrow(() -> new DataNotFoundException(uuid + " uuid post image not found"));
    awsS3Util.delete(postImage.getS3Key());
    postImageRepository.deleteById(postImage.getId());
  }

  public void deletePostImageByPost(Post post) {
    for (PostImage postImage : post.getPostImages()) {
      deletePostImage(postImage.getUuid());
    }
  }
}
