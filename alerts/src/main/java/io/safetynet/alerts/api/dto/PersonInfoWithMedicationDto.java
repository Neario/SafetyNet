package io.safetynet.alerts.api.dto;

import io.safetynet.alerts.model.MedicalRecord;

import java.util.List;


public record PersonInfoWithMedicationDto (
     String lastName,
     String address,
     int age,
     String email,
     List<String> medications,
     List<String> allergies
) {
    public PersonInfoWithMedicationDto (MedicalRecord medicalRecord, String address, String email) {
        this(medicalRecord.getLastName(), address, medicalRecord.getAge(), email, medicalRecord.getMedications(), medicalRecord.getAllergies());
    }
}
