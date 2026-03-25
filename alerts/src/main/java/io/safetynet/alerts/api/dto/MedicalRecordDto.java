package io.safetynet.alerts.api.dto;

import io.safetynet.alerts.model.MedicalRecord;

import java.util.List;

public record MedicalRecordDto (
     String firstName,
     String lastName,
     String birthdate,
     List<String> medications,
     List<String> allergies
    ) {
    public MedicalRecordDto(MedicalRecord medicalRecord) {
        this(medicalRecord.getFirstName(), medicalRecord.getLastName(), medicalRecord.getBirthdate(), medicalRecord.getMedications(), medicalRecord.getAllergies());
    }

    public String getId() {
        return firstName + "-" + lastName;
    }
}
