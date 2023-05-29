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

    private static UserService USER_SERVICE;
    private UserDatabase db;

    private UserService(Context context) {
        db = UserDatabase.getInstance(context);
    }

    public static UserService getInstance(Context context) {
        if (USER_SERVICE == null) {
            USER_SERVICE = new UserService(context);
        }
        return USER_SERVICE;
    }

    public CompletableFuture<Void> addUser(UserInfo userInfo) {
        User user = new User(userInfo.getName(), userInfo.getGender(), userInfo.getAge(), userInfo.getDivision(), LocalDateTime.now());

        return CompletableFuture.runAsync(() -> db.userDao().save(user));
    }

    public CompletableFuture<User> getUserById(Long id) {
        return CompletableFuture.supplyAsync(() -> db.userDao().findById(id));
    }

    public CompletableFuture<Void> update(User user) {
        return CompletableFuture.runAsync(() -> db.userDao().update(user));
    }


    public CompletableFuture<List<User>> getUsers() {
        Log.i("getUsers", "사용자 정보 가져오기");
        return CompletableFuture.supplyAsync(() -> db.userDao().selectAll());
    }

    public CompletableFuture<Void> deleteUser(User user) {
        return CompletableFuture.runAsync(() -> db.userDao().delete(user));
    }

    public CompletableFuture<Void> removeAll() {
        return CompletableFuture.runAsync(() -> db.userDao().deleteAll());
    }

    
    
}
