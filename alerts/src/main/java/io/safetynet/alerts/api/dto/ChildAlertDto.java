package io.safetynet.alerts.api.dto;

import io.safetynet.alerts.model.MedicalRecord;

import java.util.List;

public record ChildAlertDto (
     String firstName,
     String lastName,
     int age,
     List<String> otherFamily
    ) {

    public ChildAlertDto (MedicalRecord medicalRecord, List<String> otherFamily) {
        this(medicalRecord.getFirstName(),  medicalRecord.getLastName(), medicalRecord.getAge(), otherFamily);
    }
}