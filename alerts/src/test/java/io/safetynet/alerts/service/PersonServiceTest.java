package io.safetynet.alerts.service;

import io.safetynet.alerts.api.exception.AlreadyExistsException;
import io.safetynet.alerts.api.exception.NotFoundException;
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
import static org.junit.jupiter.api.Assertions.*;
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

        PersonDto result = service.create(personDto);

        verify(repository).create(person);
        assertNotNull(result);
    }

    @Test
    public void processCreatePerson_alreadyExists() {
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
        when(repository.create(person)).thenThrow(new AlreadyExistsException(person.getId() + " already exists"));

        assertThrows(
                AlreadyExistsException.class,
                () -> service.create(personDto)
        );
    }

    @Test
    public void processUpdatePerson() {
        PersonDto personDto = new PersonDto(
                "John",
                "Boyd",
                "mika@mika.mail",
                "mikaAddress",
                "MikaCity",
                "MikaPhone"
        );
        Person person = new Person();

        when(repository.findById(personDto.getId())).thenReturn(Optional.of(person));

        PersonDto result = service.update(personDto);

        verify(repository).update(person);

        assertEquals("John", result.firstName());
    }

    @Test
    public void processUpdatePerson_notFound() {
        PersonDto personDto = new PersonDto(
                "John",
                "Boyd",
                "mika@mika.mail",
                "mikaAddress",
                "MikaCity",
                "MikaPhone"
        );

        when(repository.findById("John-Boyd")).thenThrow(new NotFoundException("id not found"));

        assertThrows(
                NotFoundException.class,
                () -> service.update(personDto)
        );

        verify(repository, never()).update(any());
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
        when(repository.findById(personDto.getId())).thenReturn(Optional.of(person));

        service.delete(personDto);

        verify(repository).delete(person);
    }

    @Test
    public void processDeletePerson_notFound() {
        PersonDto personDto = new PersonDto(
                "Mika",
                "Mika",
                null,
                null,
                null,
                null
        );
        Person person = new Person();

        when(repository.findById("Mika-Mika")).thenThrow(new NotFoundException("id not found"));


        assertThrows(
                NotFoundException.class,
                () -> service.delete(personDto)
        );

        verify(repository, never()).delete(any());
    }

    @ParameterizedTest
    @MethodSource("fareArguments")
    public void processChildAlert(Person person, MedicalRecord medicalRecord) {
        when(repository.findByAddress(List.of("1509 Culver St"))).thenReturn(List.of(person));
        when(medicalRecordRepository.findById(person.getId())).thenReturn(Optional.of(medicalRecord));

        List<ChildAlertDto> result = service.getChildrenByAddress("1509 Culver St");

        assertThat(result).hasSize(1);
    }

    @ParameterizedTest
    @MethodSource("fareArguments")
    public void processPersonInfoByLastName(Person person, MedicalRecord medicalRecord) {

        when(repository.findByLastName("Boyd")).thenReturn(List.of(person));
        when(medicalRecordRepository.findById(person.getId())).thenReturn(Optional.of(medicalRecord));

        List<PersonInfoWithMedicationDto> result = service.getPersonByLastName("Boyd");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }

    @Test
    public void processPersonInfoByLastName_notFound() {
        when(repository.findByLastName("Boyd")).thenReturn(List.of());
        assertThrows(NotFoundException.class, () -> service.getPersonByLastName("Boyd"));
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
