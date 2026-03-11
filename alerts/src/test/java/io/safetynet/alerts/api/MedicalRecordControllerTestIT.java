package io.safetynet.alerts.api;

import io.safetynet.alerts.api.dto.MedicalRecordDto;
import io.safetynet.alerts.api.dto.PersonDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MedicalRecordControllerTestIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateMedicalRecord() throws Exception {
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto(
                "mika",
                "mika",
                "05/12/1988",
                List.of(new String[]{"paracetamol", "nurofen"}),
                List.of(new String[]{"pollen"})
        );
        mockMvc.perform(
                post("/medicalrecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(medicalRecordDto)
                        )
        ).andExpect(status().isOk());
    }

    @Test
    public void testUpdateMedicalRecord() throws Exception {
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto(
                "John",
                "Boyd",
                "03/06/1984",
                List.of(new String[]{"paracetamol", "nurofen"}),
                List.of(new String[]{"pollen"})
        );
        mockMvc.perform(
                put("/medicalrecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(medicalRecordDto)
                        )
        ).andExpect(status().isOk());
    }

    @Test
    public void testDeleteMedicalRecord() throws Exception {
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto(
                "John",
                "Boyd",
                null,
                null,
                null);
        mockMvc.perform(
                delete("/medicalrecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(medicalRecordDto)
                        )
        ).andExpect(status().isOk());
    }
}
