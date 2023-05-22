package com.example.aiapplication.medicine.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.aiapplication.medicine.entity.Medicine;
import com.example.aiapplication.typeConverter.LocalDateTimeConverter;

@Database(entities = {Medicine.class}, version = 1)
@TypeConverters({LocalDateTimeConverter.class})
public abstract class MedicineDatabase extends RoomDatabase {

    private static MedicineDatabase INSTANCE = null;

    public abstract MedicineDao medicineDao();

    public static MedicineDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MedicineDatabase.class, "user.db").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
