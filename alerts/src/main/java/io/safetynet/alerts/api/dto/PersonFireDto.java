package io.safetynet.alerts.api.dto;

import io.safetynet.alerts.model.MedicalRecord;
import io.safetynet.alerts.model.Person;
import org.springframework.util.Assert;

import java.util.List;

public record PersonFireDto(
        String lastName,
        String phone,
        int age,
        List<String> medications,
        List<String> allergies) {

    public PersonFireDto(Person person, MedicalRecord medicalRecord) {
        this(person.getLastName(), person.getPhone(), medicalRecord.getAge(), medicalRecord.getMedications(), medicalRecord.getAllergies());
    }

}
