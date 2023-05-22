package com.example.aiapplication.user.service;

import android.content.Context;
import android.util.Log;

import com.example.aiapplication.user.dao.UserDatabase;
import com.example.aiapplication.user.entity.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserService {

    private UserDatabase db;

    public UserService(Context context) {
        db = UserDatabase.getInstance(context);
    }

    public CompletableFuture<Void> addUser(User user) {
        return CompletableFuture.runAsync(() -> db.userDao().save(user));
    }


    public CompletableFuture<List<User>> getUsers() {
        Log.i("UserService", "getUsers 메서드 실행");
        return CompletableFuture.supplyAsync(() -> db.userDao().selectAll());
    }

    public CompletableFuture<Void> removeAll() {
        return CompletableFuture.runAsync(() -> db.userDao().deleteAll());
    }

    
    
}
