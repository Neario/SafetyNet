package io.safetynet.alerts.api.dto;

import io.safetynet.alerts.model.FireStation;

public record FireStationDto (
     String address,
     String station
) {
    public FireStationDto (FireStation fireStation) {
        this(fireStation.getAddress(), fireStation.getStation());
    }
}
