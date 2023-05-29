package com.example.aiapplication.user.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.aiapplication.user.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void save(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM users WHERE ID=(:id)")
    void deleteById(Long id);

    @Query("SELECT * FROM users")
    List<User> selectAll();

    @Query("SELECT * FROM users WHERE ID=(:id)")
    User findById(Long id);

    @Query("DELETE FROM users")
    void deleteAll();

}
