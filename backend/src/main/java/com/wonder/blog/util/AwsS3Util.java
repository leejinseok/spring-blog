package com.wonder.blog.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.wonder.blog.config.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AwsS3Util {

  private final AmazonS3 amazonS3;
  private final AwsProperties awsProperties;

  public PutObjectResult upload(String key, MultipartFile file) throws IOException {
    return amazonS3.putObject(new PutObjectRequest(bucketName(), key, file.getInputStream(), new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead));
  }

  public void delete(String key) {
    amazonS3.deleteObject(bucketName(), key);
  }

  private String bucketName() {
    return awsProperties.getS3().get("bucket-name");
  }
}
