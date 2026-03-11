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
public class MedicalRecordUpdateDto {
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;
}
