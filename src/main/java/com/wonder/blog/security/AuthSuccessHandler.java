package com.wonder.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_OK);
//        response.getWriter().print(om.writeValueAsString(ResultDto.success()));
        response.getWriter().print("Login Success");
        response.getWriter().flush();
    }

}
