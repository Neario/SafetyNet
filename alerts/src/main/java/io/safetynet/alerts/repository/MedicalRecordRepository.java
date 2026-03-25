package io.safetynet.alerts.repository;

import io.safetynet.alerts.model.MedicalRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * Repository for MedicalRecord data
 * Access to MedicalRecord stored in JSON database
 */
@Repository
@RequiredArgsConstructor
public class MedicalRecordRepository {
    private final DatabaseLoader databaseLoader;

    /**
     * Returns all MedicalRecord
     * @return List of MedicalRecord
     */
    public List<MedicalRecord> findAll() {
        return databaseLoader.getDatas().getMedicalrecords();
    }

    public Optional<MedicalRecord> findByFirstNameAndLastName(String firstName, String lastName) {
        return findAll().stream().filter(medicalRecord ->
                        medicalRecord.getLastName().equals(lastName) && medicalRecord.getFirstName().equals(firstName))
                .findFirst();
    }

    /**
     * Return Optional Medical record  by id
     * @param id id (firstName, lastName)
     * @return a MedicalRecord
     */
    public Optional<MedicalRecord> findById(String id) {
        return findAll().stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    /**
     * Return a list of MedicalRecord by id
     * @param ids list id (firstName, lastName)
     * @return list of MedicalRecord
     */
    public List<MedicalRecord> findByIds(Collection<String> ids) {
        return findAll().stream().filter(m -> ids.contains(m.getId())).toList();
    }

    /**
     * Create MedicalRecord
     * @param medicalRecord MedicalRecord Data
     * @return Created MedicalRecord
     */
    public MedicalRecord create(MedicalRecord medicalRecord) {
        findAll().add(medicalRecord);
        databaseLoader.save();
        return medicalRecord;
    }

    /**
     * Update MedicalRecord
     * @param medicalRecord MedicalRecord Data
     * @return Updated MedicalRecord
     */
    public MedicalRecord update(MedicalRecord medicalRecord) {
        databaseLoader.save();
        return medicalRecord;
    }

    /**
     * Delete MedicalRecord
     * @param medicalRecord MedicalRecord Data
     */
    public void delete(MedicalRecord medicalRecord) {
        databaseLoader.getDatas().getMedicalrecords().remove(medicalRecord);
        databaseLoader.save();
    }
}
