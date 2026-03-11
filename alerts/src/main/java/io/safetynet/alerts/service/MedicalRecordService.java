package io.safetynet.alerts.service;

import io.safetynet.alerts.api.dto.MedicalRecordDto;
import io.safetynet.alerts.api.mapper.MedicalRecordMapper;
import io.safetynet.alerts.model.MedicalRecord;
import io.safetynet.alerts.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordMapper medicalRecordMapper;

    public List<MedicalRecordDto> findAll() {

        return medicalRecordRepository.findAll()
                .stream()
                .map(medicalRecordMapper::toDto)
                .toList();
    }

    public MedicalRecordDto create(MedicalRecordDto medicalRecordDto) {
        MedicalRecord medicalRecord = medicalRecordMapper.fromDto(medicalRecordDto);
        medicalRecordRepository.create(medicalRecord);
        return medicalRecordMapper.toDto(medicalRecord);
    }

    public MedicalRecordDto update(MedicalRecordDto medicalRecordDto) {
        MedicalRecord medicalRecord = medicalRecordRepository.find(
                medicalRecordDto.getFirstName(),
                medicalRecordDto.getLastName()
        ).orElseThrow(
                () -> new RuntimeException("MedicalRecord not exist")
        );
        medicalRecordMapper.update(medicalRecord, medicalRecordDto);
        medicalRecordRepository.update(medicalRecord);
        return medicalRecordMapper.toDto(medicalRecord);
    }

    public void delete(MedicalRecordDto medicalRecordDto) {
        MedicalRecord medicalRecord = medicalRecordRepository.find(
                medicalRecordDto.getFirstName(),
                medicalRecordDto.getLastName()
        ).orElseThrow(
                () -> new RuntimeException("MedicalRecord not exist")
        );
        medicalRecordRepository.delete(medicalRecord);
    }
}
