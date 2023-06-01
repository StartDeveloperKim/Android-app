package com.example.aiapplication.user.dto;

import com.example.aiapplication.user.entity.Division;
import com.example.aiapplication.user.entity.Gender;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class UserInfo {

    private final String name;
    private final int age;
    private final Gender gender;
    private final Division division;

    private Boolean isActive;

    public UserInfo(String name, int age, Gender gender, Division division) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.division = division;
    }

    public UserInfo(String name, int age, Gender gender, Division division, Boolean isActive) {
        this(name, age, gender, division);
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public Division getDivision() {
        return division;
    }

    public Boolean getActive() {
        return isActive;
    }
}
