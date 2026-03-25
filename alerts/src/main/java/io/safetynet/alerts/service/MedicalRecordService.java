package io.safetynet.alerts.service;

import io.safetynet.alerts.api.Exception.AlreadyExistsException;
import io.safetynet.alerts.api.Exception.NotFoundException;
import io.safetynet.alerts.api.dto.MedicalRecordDto;
import io.safetynet.alerts.api.mapper.MedicalRecordMapper;
import io.safetynet.alerts.model.MedicalRecord;
import io.safetynet.alerts.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for CRUD opérations MedicalRecord and queries information
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordMapper medicalRecordMapper;

    /**
     * Returns all MedicalRecord
     * @return list of MedicalRecord
     */
    public List<MedicalRecordDto> findAll() {
        return medicalRecordRepository.findAll()
                .stream()
                .map(MedicalRecordDto::new)
                .toList();
    }

    /**
     * Create a new MedicalRecord
     * @param medicalRecordDto MedicalRecord Data
     * @return created MedicalRecord
     * @throws AlreadyExistsException if MedicalRecord already exists
     */
    public MedicalRecordDto create(MedicalRecordDto medicalRecordDto) {
        boolean exists = medicalRecordRepository.findById(medicalRecordDto.getId()).isPresent();
        if (exists) {
            throw new AlreadyExistsException("MedicalRecord already exists");
        }
        MedicalRecord medicalRecord = medicalRecordMapper.fromDto(medicalRecordDto);
        medicalRecordRepository.create(medicalRecord);
        log.info(
                "MedicalRecord created {}",
                medicalRecordDto.getId()
        );
        return medicalRecordDto;
    }

    /**
     * Update a MedicalRecord
     * @param medicalRecordDto MedicalRecord data to update
     * @return updated MedicalRecord
     * @throws NotFoundException if the FireStation not found
     */
    public MedicalRecordDto update(MedicalRecordDto medicalRecordDto) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(
                medicalRecordDto.getId()
        ).orElseThrow(
                () -> new NotFoundException("MedicalRecord not found")
        );
        medicalRecordMapper.update(medicalRecord, medicalRecordDto);
        medicalRecordRepository.update(medicalRecord);
        log.info(
                "MedicalRecord updated {}",
                medicalRecordDto.getId()
        );
        return medicalRecordDto;
    }

    /**
     * Delete a MedicalRecord
     * @param medicalRecordDto MedicalRecord to delete
     * @throws NotFoundException if MedicalRecord not found
     */
    public void delete(MedicalRecordDto medicalRecordDto) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(
                medicalRecordDto.getId()
        ).orElseThrow(
                () -> new NotFoundException("MedicalRecord not found")
        );
        medicalRecordRepository.delete(medicalRecord);
    }
}
