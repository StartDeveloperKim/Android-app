package com.example.aiapplication.medicine.dto;

public class MedicineInfo {

    private final String company;
    private final String name;
    private final String info;
    private final String dangerInfo; // TODO :: 데이터가 65이상 이런 형식으로 들어옴 나중에 파싱작업 필요할 수도

    public MedicineInfo(String company, String name, String info, String dangerInfo) {
        this.company = company;
        this.name = name;
        this.info = info;
        this.dangerInfo = dangerInfo;
    }

    public String getCompany() {
        return company;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getDangerInfo() {
        return dangerInfo;
    }
}
