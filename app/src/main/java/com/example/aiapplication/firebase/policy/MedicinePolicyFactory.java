package com.example.aiapplication.firebase.policy;

public class MedicinePolicyFactory {

    // 팩토리 패턴 DBKey 추가와 정책 추가에 대한 변경 점이 이 곳으로 모임
    public static MedicinePolicy getMedicinePolicyInstance(String DBKey) {
        if (DBKey.equals("AGE")) {
            return new AgeMedicinePolicy();
        } else if (DBKey.equals("PREGNANT_WOMAN")) {
            return new PregnantWomanMedicinePolicy();
        } else if (DBKey.equals("MULTIPLE_MEDICINE")) {
            return new MultipleMedicinePolicy();
        }
        return null; // 나중에 수정
    }
}
