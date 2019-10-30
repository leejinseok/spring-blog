package com.wonder.blog.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.wonder.blog.config.AwsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class AwsS3Util {
  private final static String BUCKET_NAME = "leejinseok-blog";
  private AmazonS3 amazonS3;
  private final AwsProperties awsProperties;

  @Autowired
  public AwsS3Util(AwsProperties awsProperties) {
    this.awsProperties = awsProperties;
    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey(), secretKey());
    amazonS3 = AmazonS3ClientBuilder.standard()
      .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
      .withRegion(Regions.AP_NORTHEAST_2)
      .build();
  }

  public PutObjectResult upload(String key, MultipartFile file) throws IOException {
    return amazonS3.putObject(new PutObjectRequest(BUCKET_NAME, key, file.getInputStream(), new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead));
  }

  public void delete(String key) {
    amazonS3.deleteObject(BUCKET_NAME, key);
  }

  private String accessKey() {
    return awsProperties.getS3().get("access-key");
  }

  private String secretKey() {
    return awsProperties.getS3().get("secret-key");
  }
}
