package com.example.aiapplication.user.entity;

import lombok.Getter;

@Getter
public enum Division {
    CHILDREN("어린이"), PREGNANT_WOMAN("임산부"), SENIOR("어르신");

    private final String name;


    Division(String name) {
        this.name = name;
    }
}
