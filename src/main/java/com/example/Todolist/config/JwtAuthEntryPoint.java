package com.example.Todolist.config;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.Todolist.domain.RestResponse;
import com.example.Todolist.service.error.GlobalException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private final GlobalException globalExceptionHandler;
    private final ObjectMapper objectMapper;

    public JwtAuthEntryPoint(GlobalException globalExceptionHandler, ObjectMapper objectMapper) {
        this.globalExceptionHandler = globalExceptionHandler;
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        // Mặc định
        ResponseEntity<?> responseEntity;

        Throwable cause = authException.getCause(); // Thường là JwtException

        if (cause instanceof JwtValidationException ex) {
            responseEntity = globalExceptionHandler.handleJwtValidationException(ex);
        } else if (cause instanceof BadJwtException ex) {
            responseEntity = globalExceptionHandler.handleBadJwtException(ex);
        } else if (cause instanceof JwtException ex) {
            responseEntity = globalExceptionHandler.handleJwtException(ex);
        } else {
            // Fallback nếu không đúng loại
            responseEntity = ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body(new RestResponse<>(401, authException.getMessage(), "Xác thực thất bại", null));
        }

        // Ghi response
        String json = objectMapper.writeValueAsString(responseEntity.getBody());
        response.getWriter().write(json);
    }
}