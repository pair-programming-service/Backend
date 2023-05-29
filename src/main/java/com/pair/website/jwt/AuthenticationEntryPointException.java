package com.pair.website.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pair.website.dto.response.BaseResponseDto;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointException implements
        AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                new ObjectMapper().writeValueAsString(
                        BaseResponseDto.fail("BAD_REQUEST", "Token의 기한이 만료되었습니다.")
                )
        );
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}