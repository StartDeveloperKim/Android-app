package com.example.aiapplication.medicine.dto;

public class MultiMedicineResponse {

    private final String symptom; // 증상

    public MultiMedicineResponse(String symptom) {
        this.symptom = symptom;
    }

    public String getSymptom() {
        return symptom;
    }
}
