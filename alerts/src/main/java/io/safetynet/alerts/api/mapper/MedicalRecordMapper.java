package io.safetynet.alerts.api.mapper;

import io.safetynet.alerts.api.dto.MedicalRecordDto;
import io.safetynet.alerts.model.MedicalRecord;
import org.springframework.stereotype.Component;

@Component
public class MedicalRecordMapper {

    public MedicalRecordDto toDto(MedicalRecord medicalRecord) {
        return MedicalRecordDto.builder()
                .firstName(medicalRecord.getFirstName())
                .lastName(medicalRecord.getLastName())
                .birthdate(medicalRecord.getBirthdate())
                .medications(medicalRecord.getMedications())
                .allergies(medicalRecord.getAllergies())
                .build();
    }

    public MedicalRecord fromDto(MedicalRecordDto medicalRecordDto) {

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName(medicalRecordDto.getFirstName());
        medicalRecord.setLastName(medicalRecordDto.getLastName());
        medicalRecord.setBirthdate(medicalRecordDto.getBirthdate());
        medicalRecord.setMedications(medicalRecordDto.getMedications());
        medicalRecord.setAllergies(medicalRecordDto.getAllergies());

        return medicalRecord;
    }

    public void update(MedicalRecord medicalRecord, MedicalRecordDto medicalRecordDto) {

        medicalRecord.setLastName(medicalRecordDto.getLastName());
        medicalRecord.setBirthdate(medicalRecordDto.getBirthdate());
        medicalRecord.setMedications(medicalRecordDto.getMedications());
        medicalRecord.setAllergies(medicalRecordDto.getAllergies());
    }
}
