package com.wonder.blog.config;

import com.wonder.blog.domain.Post;
import com.wonder.blog.domain.PostTag;
import com.wonder.blog.domain.User;
import com.wonder.blog.repository.PostRepository;
import com.wonder.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Configuration
public class AppConfig {

  @Bean
  @Profile("dev")
  public ApplicationRunner applicationRunner() {
    return new ApplicationRunner() {

      @Autowired
      UserRepository userRepository;

      @Autowired
      PostRepository postRepository;

      @Autowired
      BCryptPasswordEncoder bCryptPasswordEncoder;

      @Override
      public void run(ApplicationArguments args) {

        User user = User.builder()
          .email("sonaky47@naver.com")
          .name("leejinseok")
          .password(bCryptPasswordEncoder.encode("1111"))
          .build();

        userRepository.save(user);

        Collection<PostTag> postTags = new ArrayList<>();
        PostTag postTag = PostTag.builder().text("Java").build();
        postTags.add(postTag);

        Post post = Post.builder()
          .title("자자 ...")
          .user(user)
          .content("무엇을 쓸까 ...")
          .postTags(postTags)
          .createdAt(LocalDateTime.now())
          .build();

        postTag.setPost(post);
        postRepository.save(post);
      }
    };
  }
}
