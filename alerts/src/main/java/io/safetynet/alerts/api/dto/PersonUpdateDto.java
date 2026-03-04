package io.safetynet.alerts.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
@Builder
public class PersonUpdateDto {
    private String email;
    private String address;
    private String city;
    private String phone;
}
