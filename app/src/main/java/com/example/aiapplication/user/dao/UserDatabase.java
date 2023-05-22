package com.example.aiapplication.user.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.aiapplication.typeConverter.LocalDateTimeConverter;
import com.example.aiapplication.user.entity.User;

import lombok.Synchronized;

@Database(entities = {User.class}, version = 1)
@TypeConverters({LocalDateTimeConverter.class})
public abstract class UserDatabase extends RoomDatabase {

    private static UserDatabase INSTANCE = null;

    public abstract UserDao userDao();

    @Synchronized
    public static UserDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, "user.db").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
