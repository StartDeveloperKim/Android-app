package com.example.aiapplication.firebase.policy;

import com.example.aiapplication.medicine.dto.MedicineInfo;
import com.google.firebase.firestore.DocumentSnapshot;

public class AgeMedicinePolicy implements MedicinePolicy{
    @Override
    public MedicineInfo getMedicineInfo(DocumentSnapshot result, int age, String code) {
        String name = result.getString("name");
        String company = result.getString("company");
        String info = result.getString("age_limit");
        String dangerInfo = result.getString("info");
        return new MedicineInfo(code, company, name, info, dangerInfo, age);
    }
}
