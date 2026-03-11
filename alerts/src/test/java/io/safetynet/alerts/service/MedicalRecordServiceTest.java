package io.safetynet.alerts.service;

import io.safetynet.alerts.api.dto.MedicalRecordDto;
import io.safetynet.alerts.api.mapper.MedicalRecordMapper;
import io.safetynet.alerts.model.MedicalRecord;
import io.safetynet.alerts.repository.MedicalRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {
    @Mock
    private MedicalRecordRepository repository;

    @Mock
    private MedicalRecordMapper mapper;

    @InjectMocks
    private MedicalRecordService service;

    @Test
    public void processCreateMedicalRecord() {
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto(
                "mika",
                "mika",
                "05/12/1988",
                List.of(new String[]{"paracetamol", "nurofen"}),
                List.of(new String[]{"pollen"})
        );
        MedicalRecord medicalRecord = new MedicalRecord();

        when(mapper.fromDto(medicalRecordDto)).thenReturn(medicalRecord);
        when(mapper.toDto(medicalRecord)).thenReturn(medicalRecordDto);

        MedicalRecordDto result = service.create(medicalRecordDto);

        verify(repository).create(medicalRecord);
        assertNotNull(result);
    }

    @Test
    public void processUpdateMedicalRecord() {
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto(
                "mika",
                "mika",
                "05/12/1988",
                List.of(new String[]{"paracetamol", "nurofen"}),
                List.of(new String[]{"pollen"})
        );
        MedicalRecord medicalRecord = new MedicalRecord();

        when(repository.find("mika","mika")).thenReturn(Optional.of(medicalRecord));
        when(mapper.toDto(medicalRecord)).thenReturn(medicalRecordDto);

        MedicalRecordDto result = service.update(medicalRecordDto);

        verify(repository).update(medicalRecord);

        assertEquals("mika", result.getFirstName());
    }

    @Test
    public void processDeleteMedicalRecord() {
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto(
                "mika",
                "mika",
                null,
                null,
                null
        );
        MedicalRecord medicalRecord = new MedicalRecord();
        when(repository.find("mika","mika")).thenReturn(Optional.of(medicalRecord));

        service.delete(medicalRecordDto);

        verify(repository).delete(medicalRecord);
    }


}
