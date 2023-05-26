package com.example.aiapplication.medicine.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity
public class Medicine {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @NonNull
    private String code;

    @NonNull
    private String name;

    @NonNull
    private byte[] photo;

    @NonNull
    private String info;

    @NonNull
    private String dangerInfo;

    @NonNull
    private LocalDateTime createDate;

    @NonNull
    @ColumnInfo(name = "is_take", typeAffinity = ColumnInfo.INTEGER)
    private Boolean isTake; // 복용여부

    public Medicine() {

    }

    public Medicine(@NonNull String code, @NonNull String name, @NonNull byte[] photo, @NonNull String info, @NonNull String dangerInfo, @NonNull LocalDateTime createDate, @NonNull Boolean isTake) {
        this.code = code;
        this.name = name;
        this.photo = photo;
        this.info = info;
        this.dangerInfo = dangerInfo;
        this.createDate = createDate;
        this.isTake = isTake;
    }

    public Bitmap byteArrayToBitmap() {
        return BitmapFactory.decodeByteArray(this.photo, 0, photo.length);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getCode() {
        return code;
    }

    public void setCode(@NonNull String code) {
        this.code = code;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(@NonNull byte[] photo) {
        this.photo = photo;
    }

    @NonNull
    public String getInfo() {
        return info;
    }

    public void setInfo(@NonNull String info) {
        this.info = info;
    }

    @NonNull
    public String getDangerInfo() {
        return dangerInfo;
    }

    public void setDangerInfo(@NonNull String dangerInfo) {
        this.dangerInfo = dangerInfo;
    }

    @NonNull
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(@NonNull LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @NonNull
    public Boolean getTake() {
        return isTake;
    }

    public void setTake(@NonNull Boolean take) {
        isTake = take;
    }
}
