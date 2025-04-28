package com.example.Todolist.domain;

import java.time.Instant;

import com.example.Todolist.util.SecurityUtil;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "todolists")
public class Todolist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String status;
    private Instant createAt;
    private String createBy;
    private Instant UpdateAt;
    private String UpdateBy;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // call-back function handle after todolist creating

    @PrePersist
    public void handleAfterCreatingTodolist() {
        this.createBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "?";
        this.createAt = Instant.now();
    }

    @PreUpdate
    public void handleAfterUpdatingTodolist() {
        this.UpdateBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "?";
        this.UpdateAt = Instant.now();
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Instant getUpdateAt() {
        return UpdateAt;
    }

    public void setUpdateAt(Instant updateAt) {
        UpdateAt = updateAt;
    }

    public String getUpdateBy() {
        return UpdateBy;
    }

    public void setUpdateBy(String updateBy) {
        UpdateBy = updateBy;
    }

}
