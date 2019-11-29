package com.wonder.blog.service;

import com.wonder.blog.common.CurrentUser;
import com.wonder.blog.domain.Post;
import com.wonder.blog.domain.User;
import com.wonder.blog.dto.PostDto;
import com.wonder.blog.exception.AccessNotOwnedResourceException;
import com.wonder.blog.exception.DataNotFoundException;
import com.wonder.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final UserService userService;

  public Post addPost(PostDto.RegisterReq dto) {
    Post post = new Post();
    post.setTitle(dto.getTitle());
    post.setContent(dto.getContent());
    post.setPostTags(dto.getPostTags());
    post.setUser(dto.getUser());
    return postRepository.save(post);
  }

//  @Transactional(readOnly = true)
//  public Page<Post> getPosts(Pageable pageable) {
//    return postRepository.findAll(pageable);
//  }

  @Transactional(readOnly = true)
  public Page<Post> getPosts(Pageable pageable, String q) {
    if (q == null) q = "";
    return postRepository.findPosts(pageable, q);
  }

  @Transactional(readOnly = true)
  public Post getPost(String uuid) {
    return postRepository.findByUuid(uuid).orElseThrow(() -> new DataNotFoundException("Post uuid: " + uuid + " not founded"));
  }

  @Transactional
  public Post updatePost(String uuid, PostDto.UpdateReq dto) {
    // 영속성으로 분류되어 set 만으로 update가 가능하다는 점 ...
    Post post = getPost(uuid);
    post.setTitle(dto.getTitle());
    post.setContent(dto.getContent());
    post.clearAndAddPostTags(dto.getPostTags());
    return post;
  }

  public void deletePost(String uuid) {
    User user = userService.getUserByEmail(CurrentUser.create().getEmail());
    Post post = getPost(uuid);
    if (!post.getUser().getId().equals(user.getId())) {
      throw new AccessNotOwnedResourceException("This post is not yours");
    }

    postRepository.deleteByUuid(uuid);
  }
}
