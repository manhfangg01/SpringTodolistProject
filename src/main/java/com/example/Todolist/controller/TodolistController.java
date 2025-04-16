package com.example.Todolist.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Todolist.domain.RestResponse;
import com.example.Todolist.domain.Todolist;
import com.example.Todolist.service.TodoService;

@RestController
public class TodolistController {
    private final TodoService todoService;

    public TodolistController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/Todolist")
    public ResponseEntity<RestResponse<Todolist>> createTodolist(@RequestBody Todolist Todolist) {
        RestResponse<Todolist> response = new RestResponse<Todolist>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(Todolist);
        response.setError(null);
        response.setMessage("Tạo bảng công việc thành công !");
        this.todoService.handleSaveToDoList(Todolist);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/Todolists")
    public ResponseEntity<RestResponse<List<Todolist>>> fetchAllTodolist() {
        RestResponse<List<Todolist>> response = new RestResponse<List<Todolist>>();
        List<Todolist> Todolists = this.todoService.handleFetchAllToDoList();
        response.setStatusCode(HttpStatus.OK.value());
        if (Todolists.isEmpty()) {
            response.setMessage("Không có danh sách nào khả dụng trong hệ thống");
            response.setData(null);
        } else {
            response.setMessage("Tất cả danh sách đã tạo");
            response.setData(Todolists);
        }
        response.setError(null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/Todolist/{id}")
    public ResponseEntity<RestResponse<Todolist>> fetchTodolist(@PathVariable("id") long id) {
        RestResponse<Todolist> response = new RestResponse<Todolist>();
        Optional<Todolist> checkTodolist = this.todoService.handleFetchSpecificToDoList(id);
        if (checkTodolist.isPresent()) {
            response.setStatusCode(HttpStatus.OK.value());
            response.setData(checkTodolist.get());
            response.setError(null);
            response.setMessage("Danh sách " + id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setData(null);
            response.setError("BAD_REQUEST");
            response.setMessage("Không tồn tại danh sách " + id + " trong hệ thống");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("Todolist")
    public ResponseEntity<RestResponse<Todolist>> updateTodolist(
            @RequestBody Todolist Todolist) {
        RestResponse<Todolist> response = new RestResponse<Todolist>();
        response.setStatusCode(HttpStatus.OK.value());
        Todolist currentTodolist = this.todoService.handleFetchSpecificToDoList(Todolist.getId()).get();
        currentTodolist.setTitle(Todolist.getTitle());
        currentTodolist.setStatus(Todolist.getStatus());
        response.setData(currentTodolist);
        response.setError(null);
        response.setMessage("Cập nhật danh sách thành công");
        this.todoService.handleSaveToDoList(currentTodolist);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("Todolist/{id}")
    public ResponseEntity<RestResponse<Todolist>> deleteTodolist(@PathVariable("id") long id) {
        RestResponse<Todolist> response = new RestResponse<Todolist>();
        response.setStatusCode(HttpStatus.NO_CONTENT.value());
        response.setData(null);
        response.setError(null);
        response.setMessage("Xóa danh sách thành công");
        this.todoService.handleDeleteToDoList(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @DeleteMapping("Todolists")
    public ResponseEntity<RestResponse<Todolist>> deleteAllTodolist() {
        RestResponse<Todolist> response = new RestResponse<Todolist>();
        response.setStatusCode(HttpStatus.NO_CONTENT.value());
        response.setData(null);
        response.setError(null);
        response.setMessage("Xóa tất cả danh sách thành công");
        this.todoService.handleDeleteAllToDoList();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

}
