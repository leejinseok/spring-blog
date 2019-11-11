package com.wonder.blog.service;

import com.wonder.blog.common.TestDescription;
import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.User;
import com.wonder.blog.exception.DataNotFoundException;
import com.wonder.blog.repository.PostRepository;
import com.wonder.blog.service.PostService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PostRemove;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PostServiceTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Autowired
  PostService postService;

  @Test
  @TestDescription("존재하지 않는 게시글 조회하는 경우")
  public void getPost() {
    expectedException.expect(DataNotFoundException.class);
    postService.getPost(10);
  }

  @Test
  public void getPosts() {
    postService.getPosts(PageRequest.of(0, 10));
  }
}
