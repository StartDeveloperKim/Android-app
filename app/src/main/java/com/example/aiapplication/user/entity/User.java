package com.example.aiapplication.user.entity;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @NonNull
    private String gender;

    @NonNull
    private int age;

    @NonNull
    private String division; // 어르신, 임산부, 어린이 등등 구분

    private LocalDateTime createAt;


    public User(@NonNull Gender gender, int age, Division division, LocalDateTime createAt) {
        // TODO :: 나이에 대한 검사구문이 필요할 수도?
        this.gender = gender.getGender();
        this.age = age;
        this.division = division.getName();
        this.createAt = createAt;
    }

    // 업데이트 화면에서 정보 일괄 업데이트
    public void updateUser(Gender gender, Division division, int age) {
        this.gender = gender.getGender();
        this.division = division.getName();
        this.age = age;
    }
}
