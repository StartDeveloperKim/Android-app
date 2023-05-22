package com.example.aiapplication.medicine.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.aiapplication.medicine.entity.Medicine;

import java.util.List;

@Dao
public interface MedicineDao {

    @Insert
    void save(Medicine medicine);

    @Update
    void update(Medicine medicine);

    @Delete
    void delete(Medicine medicine);

    @Query("SELECT * FROM MEDICINE WHERE (ID=:id)")
    Medicine findById(Long id);

    @Query("SELECT * FROM MEDICINE")
    List<Medicine> selectAll();
}
