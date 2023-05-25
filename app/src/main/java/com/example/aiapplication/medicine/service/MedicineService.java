package com.example.aiapplication.medicine.service;

import android.content.Context;

import com.example.aiapplication.medicine.dao.MedicineDatabase;
import com.example.aiapplication.medicine.dto.MedicineInfo;
import com.example.aiapplication.medicine.entity.Medicine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MedicineService {

    private MedicineDatabase db;

    public MedicineService(Context context) {
        this.db = MedicineDatabase.getInstance(context);
    }

    public CompletableFuture<Void> addMedicine(MedicineInfo medicineInfo, byte[] image) {
        Medicine medicine = new Medicine(medicineInfo.getCode(), medicineInfo.getName(),
                image, medicineInfo.getInfo(), medicineInfo.getDangerInfo(), LocalDateTime.now(), Boolean.FALSE);
        return CompletableFuture.runAsync(() -> db.medicineDao().save(medicine));
    }

    public CompletableFuture<List<Medicine>> getMedicines() {
        return CompletableFuture.supplyAsync(() -> db.medicineDao().selectAll());
    }

    public CompletableFuture<Medicine> getMedicineById(Long id) {
        return CompletableFuture.supplyAsync(() -> db.medicineDao().findById(id));
    }
}
