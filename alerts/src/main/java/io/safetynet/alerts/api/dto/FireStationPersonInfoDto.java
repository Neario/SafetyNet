package io.safetynet.alerts.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FireStationPersonInfoDto {
    private List<PersonInfoDto> persons;
    private int adults;
    private int children;

    public FireStationPersonInfoDto(List<PersonInfoDto> persons, int adults) {
        this.persons = persons;
        this.adults = adults;
        this.children = persons.size() -  adults;
    }
}
