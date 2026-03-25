package io.safetynet.alerts.api.controller;

import io.safetynet.alerts.api.dto.MedicalRecordDto;
import io.safetynet.alerts.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    @GetMapping("/medicalrecords")
    public List<MedicalRecordDto> index() {
        return medicalRecordService.findAll();
    }

    /**
     * Create a MedicalRecord
     * @param medicalRecordDto MedicalRecord data (firstName, lastName, birthdate, medications, allergies)
     * @return MedicalRecord id Created
     */
    @PostMapping("/medicalrecord")
    public ResponseEntity<String> create(@RequestBody MedicalRecordDto medicalRecordDto) {
        MedicalRecordDto result = medicalRecordService.create(medicalRecordDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result.getId());
    }

    /**
     * Update a MedicalRecord
     * @param medicalRecordDto MedicalRecord data
     * @return MedicalRecord id Updated
     */
    @PutMapping("/medicalrecord")
    public ResponseEntity<String> update(@RequestBody MedicalRecordDto medicalRecordDto) {
        MedicalRecordDto result = medicalRecordService.update(medicalRecordDto);
        return ResponseEntity.status(HttpStatus.OK).body(result.getId());
    }

    /**
     * Delete a MedicalRecord
     * @param medicalRecordDto MedicalRecord data
     * @return Http Response
     */
    @DeleteMapping("/medicalrecord")
    public ResponseEntity<?> delete(@RequestBody MedicalRecordDto medicalRecordDto) {
        medicalRecordService.delete(medicalRecordDto);
        return ResponseEntity.ok().build();
    }
}
