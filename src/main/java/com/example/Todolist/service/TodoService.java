package com.example.Todolist.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.Todolist.domain.Todolist;
import com.example.Todolist.repository.TodoReponsitory;

@Service
public class TodoService {
    private final TodoReponsitory todoReponsitory;

    public TodoService(TodoReponsitory todoReponsitory) {
        this.todoReponsitory = todoReponsitory;
    }

    public void handleSaveToDoList(Todolist todolist) {
        this.todoReponsitory.save(todolist);
    }

    public List<Todolist> handleFetchAllToDoList() {
        return this.todoReponsitory.findAll();
    }

    public Optional<Todolist> handleFetchSpecificToDoList(long id) {
        return this.todoReponsitory.findById(id);
    }

    public void handleDeleteToDoList(long id) {
        this.todoReponsitory.deleteById(id);
    }

    public void handleDeleteAllToDoList() {
        this.todoReponsitory.deleteAll();
    }
}
