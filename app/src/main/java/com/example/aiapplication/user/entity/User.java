package com.example.aiapplication.user.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.aiapplication.user.dto.UserInfo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.ToString;

@ToString
@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String gender;

    @NonNull
    private int age;

    @NonNull
    private String division; // 어르신, 임산부, 어린이 등등 구분

    private LocalDateTime createAt;

    public User() {

    }


    @Builder
    public User(@NonNull String name, Gender gender, int age, Division division, LocalDateTime createAt) {
        // TODO :: 나이에 대한 검사구문이 필요할 수도?
        this.name = name;
        this.gender = gender.getGender();
        this.age = age;
        this.division = division.getName();
        this.createAt = createAt;
    }

    public boolean equalsGender(Gender gender) {
        return this.gender.equals(gender.getGender());
    }

    public boolean equalsDivision(Division division) {
        return this.division.equals(division.getName());
    }

    public void update(UserInfo userInfo) {
        this.name = userInfo.getName();
        this.gender = userInfo.getGender().getGender();
        this.division = userInfo.getDivision().getName();
        this.age = userInfo.getAge();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGender(@NonNull String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setDivision(@NonNull String division) {
        this.division = division;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public Long getId() {
        return id;
    }

    @NonNull
    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    @NonNull
    public String getDivision() {
        return division;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

}
