package io.safetynet.alerts.service;

import io.safetynet.alerts.api.Exception.AlreadyExistsException;
import io.safetynet.alerts.api.Exception.NotFoundException;
import io.safetynet.alerts.api.dto.FireStationDto;
import io.safetynet.alerts.api.dto.MedicalRecordDto;
import io.safetynet.alerts.api.dto.PersonDto;
import io.safetynet.alerts.api.mapper.MedicalRecordMapper;
import io.safetynet.alerts.model.MedicalRecord;
import io.safetynet.alerts.model.Person;
import io.safetynet.alerts.repository.MedicalRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        MedicalRecordDto result = service.create(medicalRecordDto);

        verify(repository).create(medicalRecord);
        assertNotNull(result);
    }

    @Test
    public void processCreateMedicalRecord_alreadyExists() {
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto(
                "John",
                "Boyd",
                "05/12/1988",
                List.of(new String[]{"paracetamol", "nurofen"}),
                List.of(new String[]{"pollen"})
        );
        MedicalRecord medicalRecord = new MedicalRecord();

        when(mapper.fromDto(medicalRecordDto)).thenReturn(medicalRecord);
        when(repository.create(medicalRecord)).thenThrow(new AlreadyExistsException(medicalRecord.getId() + " already exists"));

        assertThrows(
                AlreadyExistsException.class,
                () -> service.create(medicalRecordDto)
        );
    }

    @Test
    public void processUpdateMedicalRecord() {
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto(
                "John",
                "Boyd",
                "05/12/1988",
                List.of(new String[]{"paracetamol", "nurofen"}),
                List.of(new String[]{"pollen"})
        );
        MedicalRecord medicalRecord = new MedicalRecord();

        when(repository.findById(medicalRecordDto.getId())).thenReturn(Optional.of(medicalRecord));

        MedicalRecordDto result = service.update(medicalRecordDto);

        verify(repository).update(medicalRecord);

        assertEquals("John", result.firstName());
    }

    @Test
    public void processUpdateMedicalRecord_notFound() {
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto(
                "Jacob",
                "notFound",
                "05/12/1988",
                List.of(new String[]{"paracetamol", "nurofen"}),
                List.of(new String[]{"pollen"})
        );

        when(repository.findById(medicalRecordDto.getId())).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.update(medicalRecordDto)
        );

        verify(repository, never()).update(any());
    }

    @Test
    public void processDeleteMedicalRecord() {
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto(
                "Mika",
                "Mika",
                "05/12/1988",
                List.of(new String[]{"paracetamol", "nurofen"}),
                List.of(new String[]{"pollen"})
        );
        MedicalRecord medicalRecord = new MedicalRecord();
        when(repository.findById(medicalRecordDto.getId())).thenReturn(Optional.of(medicalRecord));

        service.delete(medicalRecordDto);

        verify(repository).delete(medicalRecord);
    }

    @Test
    public void processDeleteMedicalRecord_notFound() {
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto(
                "John",
                "notFound",
                null,
                null,
                null
        );
        when(repository.findById(medicalRecordDto.getId())).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.delete(medicalRecordDto)
        );

        verify(repository, never()).delete(any());
    }


}
