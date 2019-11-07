package com.wonder.blog.service;

import com.wonder.blog.common.CurrentUser;
import com.wonder.blog.dto.PostDto;
import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.PostImage;
import com.wonder.blog.entity.User;
import com.wonder.blog.exception.CustomException;
import com.wonder.blog.exception.DataNotFoundException;
import com.wonder.blog.repository.PostRepository;
import com.wonder.blog.security.UserContext;
import com.wonder.blog.util.AwsS3Util;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.comparator.Comparators;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class PostService {

  private final PostRepository postRepository;
  private final UserService userService;
  private final PostImageService postImageService;
  private final AwsS3Util awsS3Util;

//  @Autowired
//  public PostService(PostRepository postRepository, UserService userService, @Lazy PostImageService postImageService, AwsS3Util awsS3Util) {
//    this.postRepository = postRepository;
//    this.userService = userService;
//    this.postImageService = postImageService;
//    this.awsS3Util = awsS3Util;
//  }

  @Transactional
  public Post addPost(PostDto.RegisterReq dto) throws IOException {
    UserContext userContext = CurrentUser.create();
    User user = userService.getUserByEmail(userContext.getEmail());

    Post post = new Post();
    post.setTitle(dto.getTitle());
    post.setContent(dto.getContent());
    post.setUser(user);
    post.setCreatedAt(LocalDateTime.now());
    post.setUpdatedAt(LocalDateTime.now());

    postRepository.save(post);

    MultipartFile file = dto.getFile();
    if (file != null) {
      PostImage postImage = new PostImage();
      postImage.setCreatedAt(LocalDateTime.now());
      postImage.setPost(post);

      post.getPostImages().add(postImage);
      postImage.setS3Key(awsS3Util.generateS3Key(post.getId(), dto.getFile().getOriginalFilename()));
      awsS3Util.upload(postImage.getS3Key(), dto.getFile());
    }

    return post;
  }

  @Transactional(readOnly = true)
  public Page<Post> getPosts(Pageable pageable) {
    return postRepository.findAll(pageable);
  }

  @Transactional(readOnly = true)
  public Post getPost(int id) {
    return postRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Post id: " + id + " not founded"));
  }

  public Post updatePost(int id, Post post) {
    Post newPost = getPost(id);
    newPost.setTitle(post.getTitle());
    newPost.setContent(post.getContent());
    newPost.setUpdatedAt(LocalDateTime.now());
    return newPost;
  }

  public int deletePost(int id) {
    UserContext userContext = CurrentUser.create();
    User user = userService.getUserByEmail(userContext.getEmail());

    Post post = getPost(id);
    if (!post.getUser().getId().equals(user.getId())) {
      throw new CustomException("This post not yours");
    }

    for (PostImage postImage : postImageService.getPostImagesByPost(post)) {
      awsS3Util.delete(postImage.getS3Key());
    }

    postRepository.deleteById(id);
    return id;
  }
}
