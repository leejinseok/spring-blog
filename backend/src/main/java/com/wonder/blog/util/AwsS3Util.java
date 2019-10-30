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
  private final AmazonS3 amazonS3;
  private final AwsProperties awsProperties;

  @Autowired
  public AwsS3Util(AwsProperties awsProperties) {
    this.awsProperties = awsProperties;
    this.amazonS3 = amazonS3();
  }

  public PutObjectResult upload(String key, MultipartFile file) throws IOException {
    return amazonS3.putObject(new PutObjectRequest(bucketName(), key, file.getInputStream(), new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead));
  }

  public void delete(String key) {
    amazonS3.deleteObject(bucketName(), key);
  }

  private String bucketName() {
    return awsProperties.getS3().get("bucket-name");
  }

  private String accessKey() {
    return awsProperties.getS3().get("access-key");
  }

  private String secretKey() {
    return awsProperties.getS3().get("secret-key");
  }

  private AmazonS3 amazonS3() {
    return AmazonS3ClientBuilder.standard()
      .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey(), secretKey())))
      .withRegion(Regions.AP_NORTHEAST_2)
      .build();
  }
}
