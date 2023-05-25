package com.example.aiapplication.firebase;

import com.example.aiapplication.medicine.dto.MedicineInfo;

public interface FirebaseSuccessListener {

    void onFirebaseDataParsed(MedicineInfo medicineInfo);
}
