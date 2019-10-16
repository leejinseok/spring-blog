package com.wonder.blog.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class AwsS3Util {
  private final static String BUCKET_NAME = "leejinseok-blog";
  private final static String ACCESS_KEY = "AKIA5HDDOS7EKFWDOJ7W";
  private final static String SECRET_KEY = "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";
  private AmazonS3 amazonS3;

  public AwsS3Util() {
    AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY,SECRET_KEY);
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
}
