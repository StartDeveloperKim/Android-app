package com.example.aiapplication.user.entity;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("남자"), FEMALE("여자");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }
}
