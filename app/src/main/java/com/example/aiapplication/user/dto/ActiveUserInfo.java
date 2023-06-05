package com.example.aiapplication.user.dto;

public class ActiveUserInfo {

    private final Long id;
    private final String name;
    private final int age;
    private final String division;

    public ActiveUserInfo(Long id, String name, int age, String division) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.division = division;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getDivision() {
        return division;
    }
}
