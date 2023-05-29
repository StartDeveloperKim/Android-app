package com.example.aiapplication.user.entity;

import lombok.Getter;

public enum Division {
    ADULT("어른"), CHILDREN("어린이"), PREGNANT_WOMAN("임산부"), SENIOR("어르신");

    private final String name;


    Division(String name) {
        this.name = name;
    }

    public static Division getInstance(String value) {
        for (Division division : Division.values()) {
            if (division.getName().equals(value)) {
                return division;
            }
        }
        throw new IllegalArgumentException("Invalid Division value: " + value);

    }

    public String getName() {
        return name;
    }
}
