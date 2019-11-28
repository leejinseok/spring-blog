package com.wonder.blog.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AwsConfig {

  private final AwsProperties properties;

  @Bean
  public AmazonS3 amazonS3() {
    AWSCredentials credentials = new BasicAWSCredentials(accessKey(), secretKey());
    AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);

    return AmazonS3ClientBuilder
      .standard()
      .withCredentials(credentialsProvider)
      .withRegion(Regions.AP_NORTHEAST_2)
      .build();
  }

  private String accessKey() {
    return properties.getS3().get("access-key");
  }

  private String secretKey() {
    return properties.getS3().get("secret-key");
  }
}
