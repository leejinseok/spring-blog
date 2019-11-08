package com.wonder.blog.config;

import com.wonder.blog.entity.Post;
import com.wonder.blog.entity.User;
import com.wonder.blog.repository.PostRepository;
import com.wonder.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

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

                Post post = Post.builder()
                  .title("자자 ...")
                  .user(user)
                  .content("무엇을 쓸까 ...")
                  .createdAt(LocalDateTime.now())
                  .build();

                postRepository.save(post);
            }
        };
    }
}
