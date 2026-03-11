package io.safetynet.alerts.service;

import io.safetynet.alerts.api.dto.FireStationDto;
import io.safetynet.alerts.api.mapper.FireStationMapper;
import io.safetynet.alerts.model.FireStation;
import io.safetynet.alerts.repository.FireStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FireStationService {

    private final FireStationRepository fireStationRepository;
    private final FireStationMapper fireStationMapper;

    public List<FireStationDto> findAll()
    {
        return fireStationRepository.findAll()
                .stream().
                map(fireStationMapper::toDto)
                .toList();
    }

    public FireStationDto create(FireStationDto fireStationDto) {
        FireStation fireStation = fireStationMapper.fromDto(fireStationDto);
        fireStationRepository.create(fireStation);
        return fireStationMapper.toDto(fireStation);
    }

    public FireStationDto update(FireStationDto fireStationDto) {
        FireStation fireStation = fireStationRepository.find(
                fireStationDto.getAddress()
        ).orElseThrow(
                () -> new RuntimeException("fireStation not exist")
        );
        fireStationMapper.update(fireStation, fireStationDto);
        fireStationRepository.update(fireStation);
        return fireStationMapper.toDto(fireStation);
    }

    public void delete(FireStationDto fireStationDto) {
        FireStation fireStation = fireStationRepository.find(
                fireStationDto.getAddress()
        ).orElseThrow(
                () -> new RuntimeException("fireStation not exist")
        );
        fireStationRepository.delete(fireStation);
    }
}
