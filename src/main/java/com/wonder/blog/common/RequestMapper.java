package com.wonder.blog.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RequestMapper {
  private String url;
  private HttpMethod method;
}
