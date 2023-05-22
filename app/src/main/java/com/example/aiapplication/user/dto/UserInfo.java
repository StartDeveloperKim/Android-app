package com.example.aiapplication.user.dto;

import com.example.aiapplication.user.entity.Division;
import com.example.aiapplication.user.entity.Gender;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserInfo {

    private final String name;
    private final int age;
    private final Gender gender;
    private final Division division;

}
