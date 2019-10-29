package com.wonder.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AjaxAuthFilterTest {
  private MockMvc mvc;
  ObjectMapper mapper = new ObjectMapper();

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void findUserNotFounded() throws Exception {
    mvc.perform(post("/api/v1/auth/login")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
      .content(mapper.writeValueAsString("{\"email\": \"nouser@naver.com\", \"password\": \"1111\"}")));

    expectedException.expect(UsernameNotFoundException.class);

  }
}
