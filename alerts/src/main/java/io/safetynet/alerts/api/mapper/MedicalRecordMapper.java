package io.safetynet.alerts.api.mapper;

import io.safetynet.alerts.api.dto.MedicalRecordDto;
import io.safetynet.alerts.model.MedicalRecord;
import org.springframework.stereotype.Component;

@Component
public class MedicalRecordMapper {
    public MedicalRecord fromDto(MedicalRecordDto medicalRecordDto) {

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName(medicalRecordDto.firstName());
        medicalRecord.setLastName(medicalRecordDto.lastName());
        medicalRecord.setBirthdate(medicalRecordDto.birthdate());
        medicalRecord.setMedications(medicalRecordDto.medications());
        medicalRecord.setAllergies(medicalRecordDto.allergies());

        return medicalRecord;
    }

    public void update(MedicalRecord medicalRecord, MedicalRecordDto medicalRecordDto) {

        medicalRecord.setLastName(medicalRecordDto.lastName());
        medicalRecord.setBirthdate(medicalRecordDto.birthdate());
        medicalRecord.setMedications(medicalRecordDto.medications());
        medicalRecord.setAllergies(medicalRecordDto.allergies());
    }
}
