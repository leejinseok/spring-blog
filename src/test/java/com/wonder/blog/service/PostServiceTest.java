package com.wonder.blog.service;

import com.wonder.blog.common.TestDescription;
import com.wonder.blog.domain.Post;
import com.wonder.blog.dto.PostDto;
import com.wonder.blog.exception.DataNotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
  @Transactional
  public void addPost() {
    PostDto.RegisterReq dto = new PostDto.RegisterReq();
    dto.setTitle("글을 써보자");
    dto.setContent("내용을 ...");
    dto.setPostTags("[]");
    Post newPost = postService.addPost(dto);

    assertThat(newPost).hasFieldOrProperty("id");
  }

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
