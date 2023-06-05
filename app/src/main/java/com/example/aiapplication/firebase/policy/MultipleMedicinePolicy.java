package com.example.aiapplication.firebase.policy;

import com.example.aiapplication.medicine.dto.MedicineInfo;
import com.google.firebase.firestore.DocumentSnapshot;

public class MultipleMedicinePolicy implements MedicinePolicy{

    @Override
    public MedicineInfo getMedicineInfo(DocumentSnapshot result, int age, String code) {
        /*TODO :: 병용금기 정책 구현*/
        return null;
    }
}
