package com.example.Todolist.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Todolist.domain.User;
import com.example.Todolist.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void handleSaveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
    }

    public Optional<User> handleFetchUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public List<User> handleFetchAllUsers() {
        return this.userRepository.findAll();
    }

    public Optional<User> handleFetchUser(long id) {
        return this.userRepository.findById(id);
    }

    public Optional<User> handleGetUserByUserName(String username) {
        return this.userRepository.findByEmail(username);
    }
}
