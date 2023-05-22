package com.example.aiapplication.medicine.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
@Entity
public class Medicine {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private byte[] photo;

    @NonNull
    private LocalDateTime createDate; // 알약 체크 날짜

    @NonNull
    private Boolean isTake; // 복용여부

    public Medicine(String name, byte[] photo, LocalDateTime createDate, Boolean isTake) {
        this.name = name;
        this.photo = photo;
        this.createDate = createDate;
        this.isTake = isTake;
    }

    public void setIsTake(Boolean isTake) {
        this.isTake = isTake;
    }
}
