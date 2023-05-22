package com.example.aiapplication.user.entity;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("남자"), FEMALE("여자");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public static Gender getInstance(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.getGender().equals(value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid Gender value: " + value);
    }
}
