package io.safetynet.alerts.service;

import io.safetynet.alerts.api.dto.PersonDto;
import io.safetynet.alerts.api.mapper.PersonMapper;
import io.safetynet.alerts.model.Person;
import io.safetynet.alerts.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @Mock
    private PersonRepository repository;

    @Mock
    private PersonMapper mapper;

    @InjectMocks
    private PersonService service;

    @Test
    public void processCreatePerson() {
        PersonDto personDto = new PersonDto(
                "Mika",
                "Mika",
                "mika@mika.mail",
                "mikaAddress",
                "MikaCity",
                "MikaPhone"
        );
        Person person = new Person();

        when(mapper.fromDto(personDto)).thenReturn(person);
        when(mapper.toDto(person)).thenReturn(personDto);

        PersonDto result = service.create(personDto);

        verify(repository).create(person);
        assertNotNull(result);
    }

    @Test
    public void processUpdatePerson() {
        PersonDto personDto = new PersonDto(
                "Mika",
                "Mika",
                "mika@mika.mail",
                "mikaAddress",
                "MikaCity",
                "MikaPhone"
        );
        Person person = new Person();

        when(repository.find("Mika", "Mika")).thenReturn(Optional.of(person));
        when(mapper.toDto(person)).thenReturn(personDto);

        PersonDto result = service.update(personDto);

        verify(repository).update(person);

        assertEquals("Mika", result.getFirstName());
    }

    @Test
    public void processDeletePerson() {
        PersonDto personDto = new PersonDto(
                "Mika",
                "Mika",
                null,
                null,
                null,
                null
        );
        Person person = new Person();
        when(repository.find("Mika", "Mika")).thenReturn(Optional.of(person));

        service.delete(personDto);

        verify(repository).delete(person);
    }


}
