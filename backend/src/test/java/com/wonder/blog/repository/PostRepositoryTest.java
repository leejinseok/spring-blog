package com.wonder.blog.repository;

import com.wonder.blog.domain.Post;
import com.wonder.blog.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PostRepositoryTest {

  @Autowired
  PostRepository postRepository;

  @Test
  @Transactional
  public void addPost() {
    User user = User.builder()
      .name("jinseok")
      .email("sonaky47@naver.com")
      .password("1111")
      .build();

    Post post = Post.builder()
      .title("음 ...")
      .content("냉무")
      .user(user)
      .build();

    postRepository.save(post);
  }
}
