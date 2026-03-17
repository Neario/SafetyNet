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

    @PostMapping("/medicalrecord")
    public ResponseEntity<?> create(@RequestBody MedicalRecordDto medicalRecordDto) {
        try{
            MedicalRecordDto result = medicalRecordService.create(medicalRecordDto);
            log.info(
                    "MedicalRecord created {} {}",
                    medicalRecordDto.getFirstName(),
                    medicalRecordDto.getLastName()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(result.getFirstName() + " " + result.getLastName());
        } catch(Exception e) {
            log.error(
                    "Error created medicalRecord {} {} : {}",
                    medicalRecordDto.getFirstName(),
                    medicalRecordDto.getLastName(),
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("medicalRecord already exists");
        }
    }

    @PutMapping("/medicalrecord")
    public ResponseEntity<?> update(@RequestBody MedicalRecordDto medicalRecordDto) {
        try{
            MedicalRecordDto result = medicalRecordService.update(medicalRecordDto);
            log.info(
                    "MedicalRecord updated {} {}",
                    medicalRecordDto.getFirstName(),
                    medicalRecordDto.getLastName()
            );
            return ResponseEntity.status(HttpStatus.OK).body(result.getFirstName() + " " + result.getLastName());
        } catch(Exception e) {
            log.error(
                    "Error updating medicalRecord {} {} : {}",
                    medicalRecordDto.getFirstName(),
                    medicalRecordDto.getLastName(),
                    e.getMessage()
            );
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/medicalrecord")
    public ResponseEntity<?> delete(@RequestBody MedicalRecordDto medicalRecordDto) {
        try {
            medicalRecordService.delete(medicalRecordDto);
            log.info(
                    "medicalRecord deleted {} {}",
                    medicalRecordDto.getFirstName(),
                    medicalRecordDto.getLastName()
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(
                    "Error deleting medicalRecord {} {} : {}",
                    medicalRecordDto.getFirstName(),
                    medicalRecordDto.getLastName(),
                    e.getMessage()
            );
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
