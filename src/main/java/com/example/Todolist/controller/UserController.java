package com.example.Todolist.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.Todolist.domain.RestResponse;
import com.example.Todolist.domain.User;
import com.example.Todolist.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/users")
    public ResponseEntity<RestResponse<List<User>>> fetchAllUser() {
        RestResponse<List<User>> response = new RestResponse<List<User>>();
        List<User> users = this.userService.handleFetchAllUsers();
        response.setStatusCode(HttpStatus.OK.value());
        if (users.isEmpty()) {
            response.setMessage("Không có người dùng nào trong hệ thống");
            response.setData(null);
        } else {
            response.setMessage("Tất cả người dùng trong hệ thống");
            response.setData(users);
        }
        response.setError(null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<RestResponse<User>> getSpecificUser(@PathVariable("id") long id) {
        RestResponse<User> response = new RestResponse<User>();
        Optional<User> checkUser = this.userService.handleFetchUser(id);
        if (checkUser.isPresent()) {
            response.setStatusCode(HttpStatus.OK.value());
            response.setData(checkUser.get());
            response.setError(null);
            response.setMessage("Truy vấn thành công người dùng " + id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setData(null);
            response.setError("BAD_REQUEST");
            response.setMessage("Không có người dùng " + id + " trong hệ thống");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
