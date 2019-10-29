package com.wonder.blog.common;

import com.google.gson.GsonBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Builder
@Getter @Setter
public class ResponseWriter {
  private int status;
  private String contentType;
  private HttpServletResponse response;
  private DefaultResponse defaultResponse;

  public void write() throws IOException {
    response.setStatus(defaultResponse.getStatus());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(new GsonBuilder().create().toJson(defaultResponse));
    response.getWriter().flush();
    response.getWriter().close();
  }
}
