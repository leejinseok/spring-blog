package com.wonder.blog.config;

import com.wonder.blog.util.AwsS3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            AwsProperties awsProperties;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                System.out.println(awsProperties.getS3().get("bucket-name"));
            }
        };
    }
}
