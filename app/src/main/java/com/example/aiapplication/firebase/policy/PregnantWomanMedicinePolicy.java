package com.example.aiapplication.firebase.policy;

import com.example.aiapplication.medicine.dto.MedicineInfo;
import com.google.firebase.firestore.DocumentSnapshot;

public class PregnantWomanMedicinePolicy implements MedicinePolicy{
    @Override
    public MedicineInfo getMedicineInfo(DocumentSnapshot result, int age, String code) {
        String name = result.getString("name");
        String company = result.getString("company");
        String dangerInfo = result.getString("info");
        return new MedicineInfo(code, company, name, "해당 약은 임부금기 약품입니다.", dangerInfo);
    }
}
