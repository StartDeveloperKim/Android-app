package com.example.aiapplication.medicine.dto;

import java.io.Serializable;

public class MedicineAlarmDto implements Serializable {

    private Long id;
    private String name;
//    private String alarmTime;

    public MedicineAlarmDto(Long id, String name) {
        this.id = id;
        this.name = name;
//        this.alarmTime = alarmTime;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
