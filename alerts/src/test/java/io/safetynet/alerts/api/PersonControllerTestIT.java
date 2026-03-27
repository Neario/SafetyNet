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
        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto))
        ).andExpect(status().isCreated());
    }

    @Test
    public void testCreatePersonAlreadyExist() throws Exception {
        PersonDto personDto = new PersonDto(
                "John",
                "Boyd",
                "mika@mika.mail",
                "mikaAddress",
                "MikaCity",
                "MikaPhone");
        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto))
        ).andExpect(status().isConflict());
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
        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto))
        ).andExpect(status().isOk());
    }

    @Test
    public void testUpdatePersonNotFound() throws Exception {
        PersonDto personDto = new PersonDto(
                "Mika",
                "NotFound",
                "John@John.mail",
                "JohnAddress",
                "JohnCity",
                "JohnPhone");
        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto))
        ).andExpect(status().isNotFound());
    }

    @Test
    public void testDeletePersonNotFound() throws Exception {
        PersonDto personDto = new PersonDto(
                "John",
                "NotFound",
                null,
                null,
                null,
                null);
        mockMvc.perform(delete("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto))
        ).andExpect(status().isNotFound());
    }

    @Test
    void testChildAlert() throws Exception {
        mockMvc.perform(get("/childAlert")
                .param("address", "1509 Culver St")
        ).andExpect(status().isOk());
    }

    @Test
    void testPersonInfo() throws Exception {
        mockMvc.perform(get("/personInfo")
                .param("lastName", "Boyd")
        ).andExpect(status().isOk());
    }

    @Test
    void testPersonInfo_notFound() throws Exception {
        mockMvc.perform(get("/personInfo")
                .param("lastName", "notFound")
        ).andExpect(status().isNotFound());
    }

    @Test
    void testCommunityEmail() throws Exception {
        mockMvc.perform(get("/communityEmail")
                .param("city", "Culver")
        ).andExpect(status().isOk());
    }
}
