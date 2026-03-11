package io.safetynet.alerts.repository;

import io.safetynet.alerts.model.MedicalRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MedicalRecordRepository {
    private final DatabaseLoader databaseLoader;

    public List<MedicalRecord> findAll() {
        return databaseLoader.getDatas().getMedicalrecords();
    }

    public Optional<MedicalRecord> find(String firstName, String lastName) {
        return findAll().stream().filter(medicalRecord ->
                        medicalRecord.getLastName().equals(lastName) && medicalRecord.getFirstName().equals(firstName))
                .findFirst();
    }

    public MedicalRecord create(MedicalRecord medicalRecord) {
        boolean exists = find(medicalRecord.getFirstName(), medicalRecord.getLastName()).isPresent();
        if (exists) {
            throw new RuntimeException("MedicalRecord already exists");
        }
        findAll().add(medicalRecord);
        databaseLoader.save();
        return medicalRecord;
    }

    public MedicalRecord update(MedicalRecord medicalRecord) {
        databaseLoader.save();
        return medicalRecord;
    }

    public void delete(MedicalRecord medicalRecord) {
        databaseLoader.getDatas().getMedicalrecords().remove(medicalRecord);
        databaseLoader.save();
    }
}
