package com.wonder.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().print(om.writeValueAsString(ResultDto.fail("login fail")));
        response.getWriter().print("login fail");
        response.getWriter().flush();
    }
}
