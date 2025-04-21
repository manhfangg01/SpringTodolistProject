package com.example.Todolist.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Todolist.domain.Token;
import com.example.Todolist.domain.User;
import com.example.Todolist.domain.dto.LoginDTO;
import com.example.Todolist.service.UserService;
import com.example.Todolist.util.SecurityUtil;

@RestController
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, SecurityUtil securityUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/register")
    public User createNewUser(@RequestBody User user) {
        this.userService.handleSaveUser(user);
        return user; // Spring sẽ wrap lại thành RestResponse<User> qua FormatRestResponse
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        Optional<User> loginUser = userService.handleFetchUserByEmail(loginDTO.getUsername());

        if (loginUser.isPresent()) {
            User realUser = loginUser.get();
            if (passwordEncoder.matches(loginDTO.getPassword(), realUser.getPassword())) {
                String accessToken = securityUtil.createToken(realUser, securityUtil.getJwtExpiration());
                String refreshToken = securityUtil.createToken(realUser, securityUtil.getRefreshExpiration());

                return ResponseEntity.ok(new Token(accessToken, refreshToken));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Tên đăng nhập hoặc mật khẩu không đúng");
    }
}
