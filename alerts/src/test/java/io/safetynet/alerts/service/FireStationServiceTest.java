package io.safetynet.alerts.service;

import io.safetynet.alerts.api.dto.FireStationDto;
import io.safetynet.alerts.api.mapper.FireStationMapper;
import io.safetynet.alerts.model.FireStation;
import io.safetynet.alerts.repository.FireStationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FireStationServiceTest {
    @Mock
    private FireStationRepository repository;

    @Mock
    private FireStationMapper mapper;

    @InjectMocks
    private FireStationService service;

    @Test
    public void processCreateFireStation() {
        FireStationDto fireStationDto = new FireStationDto(
                "mikaAddress",
                "3"
        );
        FireStation fireStation = new FireStation();

        when(mapper.fromDto(fireStationDto)).thenReturn(fireStation);
        when(mapper.toDto(fireStation)).thenReturn(fireStationDto);

        FireStationDto result = service.create(fireStationDto);

        verify(repository).create(fireStation);
        assertNotNull(result);
    }

    @Test
    public void processUpdateFireStation() {
        FireStationDto fireStationDto = new FireStationDto(
                "mikaAddress",
                "3"
        );
        FireStation fireStation = new FireStation();

        when(repository.find("mikaAddress")).thenReturn(Optional.of(fireStation));
        when(mapper.toDto(fireStation)).thenReturn(fireStationDto);

        FireStationDto result = service.update(fireStationDto);

        verify(repository).update(fireStation);

        assertEquals("mikaAddress", result.getAddress());
    }

    @Test
    public void processDeleteFireStation() {
        FireStationDto fireStationDto = new FireStationDto(
                "mikaAddress",
                null
        );
        FireStation fireStation = new FireStation();
        when(repository.find("mikaAddress")).thenReturn(Optional.of(fireStation));

        service.delete(fireStationDto);

        verify(repository).delete(fireStation);
    }


}
