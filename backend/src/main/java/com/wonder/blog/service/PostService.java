package com.wonder.blog.service;

import com.wonder.blog.common.CurrentUser;
import com.wonder.blog.dto.PostDto;
import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.User;
import com.wonder.blog.exception.CustomException;
import com.wonder.blog.exception.DataNotFoundException;
import com.wonder.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

  private final PostRepository postRepository;
  private final UserService userService;

  public Post addPost(PostDto.RegisterReq dto) {
    Post post = new Post();
    post.setTitle(dto.getTitle());
    post.setContent(dto.getContent());
    post.setPostTags(dto.getPostTags());
    post.setUser(userService.getUserByEmail(CurrentUser.create().getEmail()));
    return postRepository.save(post);
  }

  @Transactional(readOnly = true)
  public Page<Post> getPosts(Pageable pageable) {
    return postRepository.findAll(pageable);
  }

  @Transactional(readOnly = true)
  public Post getPost(int id) {
    return postRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Post id: " + id + " not founded"));
  }

  @Transactional
  public Post updatePost(int id, PostDto.RegisterReq dto) {
    // 영속성으로 분류되어 set 만으로 update가 가능하다는 점 ...
    Post post = getPost(id);
    post.setTitle(dto.getTitle());
    post.setContent(dto.getContent());
    return post;
  }

  public void deletePost(int id) {
    User user = userService.getUserByEmail(CurrentUser.create().getEmail());
    Post post = getPost(id);
    if (!post.getUser().getId().equals(user.getId())) {
      throw new CustomException("This post is not yours");
    }

    postRepository.deleteById(id);
  }
}
