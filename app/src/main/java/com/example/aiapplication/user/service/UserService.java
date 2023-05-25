package com.example.aiapplication.user.service;

import android.content.Context;
import android.util.Log;

import com.example.aiapplication.user.dao.UserDatabase;
import com.example.aiapplication.user.dto.UserInfo;
import com.example.aiapplication.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserService {

    private UserDatabase db;

    public UserService(Context context) {
        db = UserDatabase.getInstance(context);
    }

    public CompletableFuture<Void> addUser(UserInfo userInfo) {
        User user = User.builder()
                .name(userInfo.getName())
                .age(userInfo.getAge())
                .createAt(LocalDateTime.now())
                .gender(userInfo.getGender())
                .division(userInfo.getDivision())
                .build();

        return CompletableFuture.runAsync(() -> db.userDao().save(user));
    }

    public CompletableFuture<User> getUserById(Long id) {
        return CompletableFuture.supplyAsync(() -> db.userDao().findById(id));
    }

    public CompletableFuture<Void> update(User user) {
        return CompletableFuture.runAsync(() -> db.userDao().update(user));
    }


    public CompletableFuture<List<User>> getUsers() {
        return CompletableFuture.supplyAsync(() -> db.userDao().selectAll());
    }

    public CompletableFuture<Void> removeAll() {
        return CompletableFuture.runAsync(() -> db.userDao().deleteAll());
    }

    
    
}
