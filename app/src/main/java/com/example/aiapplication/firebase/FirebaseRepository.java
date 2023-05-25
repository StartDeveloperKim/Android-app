package com.example.aiapplication.firebase;

import android.util.Log;

import com.example.aiapplication.medicine.dto.MedicineInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseRepository {


    private final FirebaseFirestore db;
    private final FirebaseSuccessListener listener;

    public FirebaseRepository(FirebaseSuccessListener firebaseSuccessListener) {
        db = FirebaseFirestore.getInstance();
        this.listener = firebaseSuccessListener;
    }

    public void getMedicineInfo(String medicineCode, String collectionName) {
        db.collection(collectionName)
                .document(medicineCode).get()
                .addOnSuccessListener(result -> {
                    String company = result.get("company", String.class);
                    String name = result.get("name", String.class);
                    String info = result.get("info", String.class);
                    String dangerInfo = result.get("age_limit", String.class);
                    MedicineInfo medicineInfo = new MedicineInfo(company, name, info, dangerInfo);

                    listener.onFirebaseDataParsed(medicineInfo);
                });

    }
}
