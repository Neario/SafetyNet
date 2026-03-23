package io.safetynet.alerts.service;

import io.safetynet.alerts.api.Exception.AlreadyExistsException;
import io.safetynet.alerts.api.Exception.NotFoundException;
import io.safetynet.alerts.api.dto.FireDto;
import io.safetynet.alerts.api.dto.FireStationDto;
import io.safetynet.alerts.api.dto.FireStationPersonInfoDto;
import io.safetynet.alerts.api.mapper.FireStationMapper;
import io.safetynet.alerts.model.FireStation;
import io.safetynet.alerts.model.MedicalRecord;
import io.safetynet.alerts.model.Person;
import io.safetynet.alerts.repository.DatabaseLoader;
import io.safetynet.alerts.repository.FireStationRepository;
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
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FireStationServiceTest {
    @Mock
    private FireStationRepository repository;

    @Mock
    DatabaseLoader databaseLoader;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FireStationMapper mapper;

    @InjectMocks
    private FireStationService service;

    @Test
    public void processCreateFireStation() {
        FireStationDto fireStationDto = new FireStationDto(
                "mikaAddress",
                "3"
        );
        FireStation fireStation = new FireStation();

        when(mapper.fromDto(fireStationDto)).thenReturn(fireStation);
        when(mapper.toDto(fireStation)).thenReturn(fireStationDto);

        FireStationDto result = service.create(fireStationDto);

        verify(repository).create(fireStation);
        assertNotNull(result);
    }

    @Test
    public void processCreateFireStation_alreadyExists() {
        FireStationDto fireStationDto = new FireStationDto(
                "1509 Culver St",
                "3"
        );
        FireStation fireStation = new FireStation();

        when(mapper.fromDto(fireStationDto)).thenReturn(fireStation);
        when(repository.create(fireStation)).thenThrow(new AlreadyExistsException("FireStation with address " + fireStation.getAddress() + " already exists"));

        assertThrows(
                AlreadyExistsException.class,
                () -> service.create(fireStationDto)
        );
    }

    @Test
    public void processUpdateFireStation() {
        FireStationDto fireStationDto = new FireStationDto(
                "mikaAddress",
                "3"
        );
        FireStation fireStation = new FireStation();

        when(repository.find("mikaAddress")).thenReturn(Optional.of(fireStation));
        when(mapper.toDto(fireStation)).thenReturn(fireStationDto);

        FireStationDto result = service.update(fireStationDto);

        verify(repository).update(fireStation);

        assertEquals("mikaAddress", result.getAddress());
    }

    @Test
    public void processUpdateFireStation_notFound() {
        FireStationDto fireStationDto = new FireStationDto(
                "address not found",
                "2"
        );

        when(repository.find("address not found")).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.update(fireStationDto)
        );

        verify(repository, never()).update(any());
    }

    @Test
    public void processDeleteFireStation() {
        FireStationDto fireStationDto = new FireStationDto(
                "mikaAddress",
                null
        );
        FireStation fireStation = new FireStation();
        when(repository.find("mikaAddress")).thenReturn(Optional.of(fireStation));

        service.delete(fireStationDto);

        verify(repository).delete(fireStation);
    }

    @Test
    public void processDeleteFireStation_notFound() {
        FireStationDto fireStationDto = new FireStationDto(
                "address not found",
                null
        );

        when(repository.find("address not found")).thenReturn(Optional.empty());


        assertThrows(
                NotFoundException.class,
                () -> service.delete(fireStationDto)
        );

        verify(repository, never()).delete(any());
    }

    @ParameterizedTest
    @MethodSource("fareArguments")
    public void processGetPersonsByStation(Person person, MedicalRecord medicalRecord) {
        when(repository.findByStation("1")).thenReturn(List.of("1509 Culver St"));
        when(personRepository.findByAddress(List.of("1509 Culver St"))).thenReturn(List.of(person));
        when(medicalRecordRepository.findByIds(Set.of("Mika-Boyd"))).thenReturn(List.of(medicalRecord));

        FireStationPersonInfoDto result = service.getPersonsByStation("1");

        assertThat(result).isNotNull();
        assertThat(result.getChildren()).isEqualTo(1);
        assertThat(result.getAdults()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("fareArguments")
    public void processGetPhoneNumbersByStation(Person person, MedicalRecord medicalRecord) {
        when(repository.findByStation("1")).thenReturn(List.of("1509 Culver St"));
        when(personRepository.findByAddress(List.of("1509 Culver St"))).thenReturn(List.of(person));

        List<String> result = service.getPhoneNumbersByStation("1");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo("1234567890");
    }

    @Test
    public void processGetPhoneNumbersByStation_notFound() {
        when(repository.findByStation("address not found")).thenReturn(List.of());
        assertThrows(NotFoundException.class, () -> service.getPhoneNumbersByStation("address not found"));
    }

    @ParameterizedTest
    @MethodSource("fareArgumentsWithFireStation")
    public void processGetByAddress(Person person, MedicalRecord medicalRecord, FireStation fireStation) {
        when(repository.findByAddress("1509 Culver St")).thenReturn(Optional.of(fireStation));
        when(personRepository.findByAddress("1509 Culver St")).thenReturn(List.of(person));
        when(medicalRecordRepository.findById("Mika-Boyd")).thenReturn(Optional.of(medicalRecord));


        FireDto result = service.getByAddress("1509 Culver St");

        assertThat(result).isNotNull();
        assertThat(result.getStation()).isEqualTo("1");
        assertThat(result.getPersons()).hasSize(1);
    }

    @Test
    public void processGetByAddress_notFound() {
        when(repository.findByAddress("address not found")).thenReturn((Optional.empty()));
        assertThrows(NotFoundException.class, () -> service.getByAddress("address not found"));
    }

    private static Stream<Arguments> fareArguments(){
        Person person = new Person();
        person.setFirstName("Mika");
        person.setLastName("Boyd");
        person.setCity("city");
        person.setAddress("1509 Culver St");
        person.setEmail("email");
        person.setPhone("1234567890");

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setBirthdate("12/05/2020");
        medicalRecord.setMedications(List.of("paracetamol"));
        medicalRecord.setAllergies(List.of("pollen"));

        return Stream.of(
                Arguments.of(person, medicalRecord)
        );
    }

    private static Stream<Arguments> fareArgumentsWithFireStation(){
        Person person = new Person();
        person.setFirstName("Mika");
        person.setLastName("Boyd");
        person.setCity("city");
        person.setAddress("1509 Culver St");
        person.setEmail("email");
        person.setPhone("1234567890");

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setBirthdate("12/05/2020");
        medicalRecord.setMedications(List.of("paracetamol"));
        medicalRecord.setAllergies(List.of("pollen"));

        FireStation fireStation = new FireStation();
        fireStation.setAddress("1509 Culver St");
        fireStation.setStation("1");

        return Stream.of(
                Arguments.of(person, medicalRecord,  fireStation)
        );
    }
}
