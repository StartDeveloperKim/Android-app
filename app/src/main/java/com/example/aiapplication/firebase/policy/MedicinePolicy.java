package com.example.aiapplication.firebase.policy;

import com.example.aiapplication.medicine.dto.MedicineInfo;
import com.google.firebase.firestore.DocumentSnapshot;

public interface MedicinePolicy {

    MedicineInfo getMedicineInfo(DocumentSnapshot result, int age, String code);
}
