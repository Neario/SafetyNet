package io.safetynet.alerts.api.mapper;

import io.safetynet.alerts.api.dto.FireStationDto;
import io.safetynet.alerts.model.FireStation;
import org.springframework.stereotype.Component;

@Component
public class FireStationMapper {

    public FireStation fromDto(FireStationDto fireStationDto) {

        FireStation fireStation = new FireStation();

        fireStation.setAddress(fireStationDto.address());
        fireStation.setStation(fireStationDto.station());
        return fireStation;
    }

    public void update(FireStation fireStation, FireStationDto fireStationDto) {

        fireStation.setStation(fireStationDto.station());
    }
}
