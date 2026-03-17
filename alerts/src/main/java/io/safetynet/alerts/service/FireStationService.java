package io.safetynet.alerts.service;

import io.safetynet.alerts.api.dto.FireStationDto;
import io.safetynet.alerts.api.dto.FireStationPersonInfoDto;
import io.safetynet.alerts.api.dto.PersonInfoDto;
import io.safetynet.alerts.api.mapper.FireStationMapper;
import io.safetynet.alerts.model.FireStation;
import io.safetynet.alerts.model.MedicalRecord;
import io.safetynet.alerts.model.Person;
import io.safetynet.alerts.repository.FireStationRepository;
import io.safetynet.alerts.repository.MedicalRecordRepository;
import io.safetynet.alerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FireStationService {

    private final FireStationRepository fireStationRepository;
    private final FireStationMapper fireStationMapper;
    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public List<FireStationDto> findAll()
    {
        return fireStationRepository.findAll()
                .stream().
                map(fireStationMapper::toDto)
                .toList();
    }

    public FireStationDto create(FireStationDto fireStationDto) {
        FireStation fireStation = fireStationMapper.fromDto(fireStationDto);
        fireStationRepository.create(fireStation);
        return fireStationMapper.toDto(fireStation);
    }

    public FireStationDto update(FireStationDto fireStationDto) {
        FireStation fireStation = fireStationRepository.find(
                fireStationDto.getAddress()
        ).orElseThrow(
                () -> new RuntimeException("fireStation not exist")
        );
        fireStationMapper.update(fireStation, fireStationDto);
        fireStationRepository.update(fireStation);
        return fireStationMapper.toDto(fireStation);
    }

    public void delete(FireStationDto fireStationDto) {
        FireStation fireStation = fireStationRepository.find(
                fireStationDto.getAddress()
        ).orElseThrow(
                () -> new RuntimeException("fireStation not exist")
        );
        fireStationRepository.delete(fireStation);
    }

    public FireStationPersonInfoDto getPersonsByStation(String station) {
        List<String> fireStationAddresses = fireStationRepository.findByStation(station);
        List<Person> persons = personRepository.findByAddress(fireStationAddresses);
        List<MedicalRecord> medicalRecords = persons.stream().map(person ->
             medicalRecordRepository.find(person.getFirstName(), person.getLastName()).orElse(null))
                .filter(Objects::nonNull).toList();

        int children = medicalRecords.stream().filter(medicalRecord -> {
            LocalDate dateNow = LocalDate.now();
            LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            int age = Period.between(birthDate, dateNow).getYears();
            return age <= 18;
        } ).toList().size();

        int adults = medicalRecords.size() - children;

        List<PersonInfoDto> personsDto =
                persons.stream().map(person ->
                        PersonInfoDto.builder()
                                .firstName(person.getFirstName())
                                .lastName(person.getLastName())
                                .address(person.getAddress())
                                .phone(person.getPhone())
                                .build()
                        ).toList();

        return new FireStationPersonInfoDto(personsDto, adults,  children);
    }
}
