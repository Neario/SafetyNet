package io.safetynet.alerts.api;

import io.safetynet.alerts.api.dto.PersonDto;
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
public class PersonControllerTestIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreatePerson() throws Exception {
        PersonDto personDto = new PersonDto(
                "Mika",
                "Mikatest",
                "mika@mika.mail",
                "mikaAddress",
                "MikaCity",
                "MikaPhone");
        mockMvc.perform(
                        post("/person")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(personDto)
                                )
                ).andExpect(status().isCreated());
    }

    @Test
    public void testUpdatePerson() throws Exception {
        PersonDto personDto = new PersonDto(
                "John",
                "Boyd",
                "John@John.mail",
                "JohnAddress",
                "JohnCity",
                "JohnPhone");
        mockMvc.perform(
                        put("/person")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(personDto)
                                )
                ).andExpect(status().isOk());
    }

    @Test
    public void testDeletePerson() throws Exception {
        PersonDto personDto = new PersonDto(
                "John",
                "Boyd",
                null,
                null,
                null,
                null);
        mockMvc.perform(
                delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(personDto)
                        )
        ).andExpect(status().isOk());
    }
}
