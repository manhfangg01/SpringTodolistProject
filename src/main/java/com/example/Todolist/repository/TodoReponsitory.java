package com.example.Todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Todolist.domain.Todolist;

@Repository
public interface TodoReponsitory extends JpaRepository<Todolist, Long> {

}
