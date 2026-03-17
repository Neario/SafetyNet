package io.safetynet.alerts.api;

import io.safetynet.alerts.api.dto.FireStationDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FireStationControllerTestIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateFireStation() throws Exception {
        FireStationDto fireStationDto = new FireStationDto(
                "MikaAddress",
                "3");
        mockMvc.perform(
                post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(fireStationDto)
                        )
        ).andExpect(status().isCreated());
    }

    @Test
    public void testUpdateFireStation() throws Exception {
        FireStationDto fireStationDto = new FireStationDto(
                "1509 Culver St",
                "5");
        mockMvc.perform(
                put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(fireStationDto)
                        )
        ).andExpect(status().isOk());
    }

    @Test
    public void testDeleteFireStation() throws Exception {
        FireStationDto fireStationDto = new FireStationDto(
                "1509 Culver St",
                null);
        mockMvc.perform(
                delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(fireStationDto)
                        )
        ).andExpect(status().isOk());
    }
}
