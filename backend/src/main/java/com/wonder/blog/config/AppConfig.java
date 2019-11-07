package com.wonder.blog.config;

import com.wonder.blog.entity.User;
import com.wonder.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AppConfig {

    @Bean
    @Profile("dev")
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            UserService userService;

            @Override
            public void run(ApplicationArguments args) {
                User user = User.builder()
                  .email("sonaky47@naver.com")
                  .password("1111")
                  .name("leejinseok")
                  .build();

                userService.addUser(user);
            }
        };
    }
}
