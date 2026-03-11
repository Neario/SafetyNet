package io.safetynet.alerts.api.mapper;

import io.safetynet.alerts.api.dto.FireStationDto;
import io.safetynet.alerts.model.FireStation;
import org.springframework.stereotype.Component;

@Component
public class FireStationMapper {

    public FireStationDto toDto(FireStation fireStation) {
        return FireStationDto.builder()
                .address(fireStation.getAddress())
                .station(fireStation.getStation())
                .build();
    }

    public FireStation fromDto(FireStationDto fireStationDto) {

        FireStation fireStation = new FireStation();

        fireStation.setAddress(fireStationDto.getAddress());
        fireStation.setStation(fireStationDto.getStation());
        return fireStation;
    }

    public void update(FireStation fireStation, FireStationDto fireStationDto) {

        fireStation.setStation(fireStationDto.getStation());
    }
}
