package com.example.Todolist.controller;

import java.security.Security;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Todolist.domain.Token;
import com.example.Todolist.domain.User;
import com.example.Todolist.domain.dto.LoginDTO;
import com.example.Todolist.domain.dto.RestLoginDTO;
import com.example.Todolist.service.UserService;
import com.example.Todolist.util.SecurityUtil;

import jakarta.validation.Valid;

@RestController
public class AuthController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final SecurityUtil securityUtil;

    public AuthController(AuthenticationManager authenticationManager, SecurityUtil securityUtil,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        this.userService.handleSaveUser(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<RestLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // create token
        String accessToken = this.securityUtil.createToken(authentication);
        RestLoginDTO res = new RestLoginDTO();
        res.setAccessToken(accessToken);
        return ResponseEntity.ok(res);
    }
}
