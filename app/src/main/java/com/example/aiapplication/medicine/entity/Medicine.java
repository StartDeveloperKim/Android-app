package com.example.aiapplication.medicine.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;

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
    @ColumnInfo(name = "is_take", typeAffinity = ColumnInfo.INTEGER)
    private Boolean isTake; // 복용여부

    public Medicine() {

    }

    public Medicine(String name, byte[] photo, LocalDateTime createDate, Boolean isTake) {
        this.name = name;
        this.photo = photo;
        this.createDate = createDate;
        this.isTake = isTake;
    }

    public void setIsTakeValue(Boolean isTake) {
        this.isTake = isTake;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setPhoto(@NonNull byte[] photo) {
        this.photo = photo;
    }

    public void setCreateDate(@NonNull LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public void setTake(@NonNull Boolean take) {
        isTake = take;
    }

    public Long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public byte[] getPhoto() {
        return photo;
    }

    @NonNull
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @NonNull
    public Boolean getTake() {
        return isTake;
    }
}
