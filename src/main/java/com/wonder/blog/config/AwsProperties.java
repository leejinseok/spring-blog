package com.wonder.blog.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "aws")
@Setter @Getter
public class AwsProperties {
    private Map<String, String> s3 = new HashMap<>();
}
