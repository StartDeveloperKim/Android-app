package com.example.aiapplication.user.entity;

public enum Division {
    ADULT("어른", "AGE"), CHILDREN("어린이", "AGE"),
    PREGNANT_WOMAN("임산부", "PREGNANT_WOMAN"), SENIOR("어르신", "AGE");

    private final String name;
    private final String DBKey;


    Division(String name, String dbKey) {
        this.name = name;
        this.DBKey = dbKey;
    }

    public static Division getInstance(String value) {
        for (Division division : Division.values()) {
            if (division.getName().equals(value)) {
                return division;
            }
        }
        throw new IllegalArgumentException("Invalid Division value: " + value);
    }

    public static String getDBKey(String value) {
        for (Division division : Division.values()) {
            if (division.getName().equals(value)) {
                return division.getDBKey();
            }
        }
        throw new IllegalArgumentException("Invalid Division value: " + value);
    }

    public String getName() {
        return name;
    }

    public String getDBKey() {
        return DBKey;
    }
}
