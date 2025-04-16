package com.example.Todolist.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.authentication.PasswordEncoderParser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Todolist.domain.RestResponse;
import com.example.Todolist.domain.User;
import com.example.Todolist.domain.UserResponse;
import com.example.Todolist.domain.dto.LoginDTO;
import com.example.Todolist.service.UserService;

@RestController
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

    }

    @PostMapping("/register")
    public ResponseEntity<RestResponse<User>> CreateNewUser(@RequestBody User user) {
        RestResponse<User> response = new RestResponse<User>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(user);
        response.setMessage("Lưu người dùng mới thành công");
        response.setError(null);

        this.userService.handleSaveUser(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<RestResponse<UserResponse>> LoginUser(@RequestBody LoginDTO loginDTO) {
        RestResponse<UserResponse> response = new RestResponse<UserResponse>();
        Optional<User> loginUser = this.userService.handleFetchUserByEmail(loginDTO.getUsername());
        if (loginUser.isPresent()) {
            User realUser = loginUser.get();
            UserResponse tempUser = new UserResponse();
            tempUser.setEmail(realUser.getEmail());
            tempUser.setFullName(realUser.getFullName());
            tempUser.setId(realUser.getId());
            if (passwordEncoder.matches(loginDTO.getPassword(), realUser.getPassword())) {
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Bạn đã đăng nhập thành công");
                response.setError(null);
                response.setData(tempUser);
                return ResponseEntity.ok(response);
            }
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        response.setMessage("Rất tiếc! Đăng nhập thất bại");
        response.setError("UNAUTHORIZED");
        response.setData(null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
