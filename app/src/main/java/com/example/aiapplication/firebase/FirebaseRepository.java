package com.example.aiapplication.firebase;

import com.example.aiapplication.firebase.policy.MedicinePolicy;
import com.example.aiapplication.medicine.dto.MedicineInfo;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseRepository {


    private final FirebaseFirestore db;
    private final FirebaseSuccessListener listener;

    public FirebaseRepository(FirebaseSuccessListener firebaseSuccessListener) {
        db = FirebaseFirestore.getInstance();
        this.listener = firebaseSuccessListener;
    }

    public void getMedicineInfo(String code, String collectionName, int age, MedicinePolicy medicinePolicy) {
        db.collection(collectionName)
                .document(code).get()
                .addOnSuccessListener(result -> {
                    MedicineInfo medicineInfo = medicinePolicy.getMedicineInfo(result, age, code);
                    listener.onFirebaseDataParsed(medicineInfo);
                });
    }
}
