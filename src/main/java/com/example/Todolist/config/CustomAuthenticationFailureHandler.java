package com.example.Todolist.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;

import com.example.Todolist.domain.RestResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        RestResponse<Object> res = new RestResponse<>();

        if (exception instanceof UsernameNotFoundException) {
            res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            res.setError("Tên đăng nhập không tồn tại");
            res.setMessage("UsernameNotFoundException");
        } else if (exception instanceof BadCredentialsException) {
            res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            res.setError("Mật khẩu không đúng");
            res.setMessage("BadCredentialsException");
        } else {
            res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            res.setError("Lỗi xác thực: " + exception.getMessage());
            res.setMessage("AuthenticationException");
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(res.toString());
    }
}