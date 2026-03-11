package io.safetynet.alerts.api.controller;

import io.safetynet.alerts.api.dto.MedicalRecordDto;
import io.safetynet.alerts.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    @GetMapping("/medicalrecords")
    public List<MedicalRecordDto> index() {
        return medicalRecordService.findAll();
    }

    @PostMapping("/medicalrecord")
    public MedicalRecordDto create(@RequestBody MedicalRecordDto medicalRecordDto) {
        return medicalRecordService.create(medicalRecordDto);
    }

    @PutMapping("/medicalrecord")
    public MedicalRecordDto update(@RequestBody MedicalRecordDto medicalRecordDto) {
        return medicalRecordService.update(medicalRecordDto);
    }

    @DeleteMapping("/medicalrecord")
    public void delete(@RequestBody MedicalRecordDto medicalRecordDto) {
        medicalRecordService.delete(medicalRecordDto);
    }
}
