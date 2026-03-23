package io.safetynet.alerts.service;

import io.safetynet.alerts.api.dto.ChildAlertDto;
import io.safetynet.alerts.api.dto.PersonDto;
import io.safetynet.alerts.api.dto.PersonInfoWithMedicationDto;
import io.safetynet.alerts.api.mapper.PersonMapper;
import io.safetynet.alerts.model.MedicalRecord;
import io.safetynet.alerts.model.Person;
import io.safetynet.alerts.repository.MedicalRecordRepository;
import io.safetynet.alerts.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @Mock
    private PersonRepository repository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

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

        when(repository.findByFirstNameAndLastName("Mika", "Mika")).thenReturn(Optional.of(person));
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
        when(repository.findByFirstNameAndLastName("Mika", "Mika")).thenReturn(Optional.of(person));

        service.delete(personDto);

        verify(repository).delete(person);
    }

    @ParameterizedTest
    @MethodSource("fareArguments")
    public void processChildAlert(Person person, MedicalRecord medicalRecord) {
        when(repository.findByAddress(List.of("1509 Culver St"))).thenReturn(List.of(person));
        when(medicalRecordRepository.findByFirstNameAndLastName("Mika","Boyd")).thenReturn(Optional.of(medicalRecord));

        List<ChildAlertDto> result = service.getChildrenByAddress("1509 Culver St");

        assertThat(result).hasSize(1);
    }

    @ParameterizedTest
    @MethodSource("fareArguments")
    public void processPersonInfoByLastName(Person person, MedicalRecord medicalRecord) {

        when(repository.findByLastName("Boyd")).thenReturn(List.of(person));
        when(medicalRecordRepository.findByFirstNameAndLastName("Mika", "Boyd")).thenReturn(Optional.of(medicalRecord));

        List<PersonInfoWithMedicationDto> result = service.getPersonByLastName("Boyd");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }

    @ParameterizedTest
    @MethodSource("fareArguments")
    public void processPersonEmailByCity(Person person, MedicalRecord medicalRecord) {
        when(repository.findByCity("city")).thenReturn(List.of(person));

        List<String> result = service.getEmailsByCity("city");

        assertEquals(1, result.size());
        assertEquals("email", result.getFirst());
    }

    private static Stream<Arguments> fareArguments(){
        Person person = new Person();
        person.setFirstName("Mika");
        person.setLastName("Boyd");
        person.setCity("city");
        person.setAddress("1509 Culver St");
        person.setEmail("email");

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setBirthdate("12/05/2020");
        medicalRecord.setMedications(List.of("paracetamol"));
        medicalRecord.setAllergies(List.of("pollen"));

        return Stream.of(
                Arguments.of(person, medicalRecord)
        );
    }
}
