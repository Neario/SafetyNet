package io.safetynet.alerts.api;

import io.safetynet.alerts.api.dto.FireStationDto;
import io.safetynet.alerts.repository.DatabaseLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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

    @Autowired
    DatabaseLoader loader;

    @BeforeEach
    void resetJson() throws Exception {
        Path source = Path.of("src/test/resources/data-test-init.json");

        Path target = Path.of("target/test-classes/data-test.json");

        Files.copy(
                source,
                target,
                StandardCopyOption.REPLACE_EXISTING
        );
        loader.reload();
    }

    @Test
    public void testCreateFireStation() throws Exception {
        FireStationDto fireStationDto = new FireStationDto(
                "MikaAddress",
                "3");
        mockMvc.perform(
                post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fireStationDto))
        ).andExpect(status().isCreated());
    }

    @Test
    public void testCreateFireStationAlreadyExists() throws Exception {
        FireStationDto fireStationDto = new FireStationDto("1509 Culver St", "3");
        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fireStationDto))
        ).andExpect(status().isConflict());
    }

    @Test
    public void testUpdateFireStation() throws Exception {
        FireStationDto fireStationDto = new FireStationDto(
                "112 Steppes Pl",
                "3");
        mockMvc.perform(
                put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(fireStationDto)
                        )
        ).andExpect(status().isOk());
    }

    @Test
    public void testUpdateFireStationNotFound() throws Exception {
        FireStationDto fireStationDto = new FireStationDto(
                "address not found",
                "5");
        mockMvc.perform(
                put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(fireStationDto)
                        )
        ).andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteFireStation() throws Exception {
        FireStationDto fireStationDto = new FireStationDto(
                "947 E. Rose Dr",
                null);
        mockMvc.perform(
                delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(fireStationDto)
                        )
        ).andExpect(status().isOk());
    }

    @Test
    public void testDeleteFireStationNotFound() throws Exception {
        FireStationDto fireStationDto = new FireStationDto(
                "address not found",
                null);
        mockMvc.perform(
                delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(fireStationDto)
                        )
        ).andExpect(status().isNotFound());
    }

    @Test
    public void testGetPersonBysStation() throws Exception {
        mockMvc.perform(
                get("/firestation")
                        .param("station", "3")
        ).andExpect(status().isOk());
    }

    @Test
    void testGetPhoneNumbersByStation() throws Exception {
        mockMvc.perform(
                get("/phoneAlert")
                        .param("station", "3")
        ).andExpect(status().isOk());
    }

    @Test
    void testGetPhoneNumbersByStationNotFound() throws Exception {
        mockMvc.perform(
                get("/phoneAlert")
                        .param("station", "6")
        ).andExpect(status().isNotFound());
    }

    @Test
    void testGetFireByAddress() throws Exception {

        mockMvc.perform(
                get("/fire")
                        .param("address", "1509 Culver St")
        ).andExpect(status().isOk());
    }

    @Test
    void testGetFireByAddressNotFound() throws Exception {

        mockMvc.perform(
                get("/fire")
                        .param("address", "address not found")
        ).andExpect(status().isNotFound());
    }

    @Test
    void testGetFlood() throws Exception {
        mockMvc.perform(
                get("/flood/stations")
                        .param("stations", "1", "3")
        ).andExpect(status().isOk());
    }
}
