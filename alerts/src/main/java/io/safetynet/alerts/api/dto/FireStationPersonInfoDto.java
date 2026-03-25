package io.safetynet.alerts.api.dto;

import java.util.List;

public record FireStationPersonInfoDto (
     List<PersonInfoDto> persons,
     int adults,
     int children
) {
    public FireStationPersonInfoDto (List<PersonInfoDto> persons, int adults) {
        this(persons, adults, persons.size() -  adults);
    }
}
