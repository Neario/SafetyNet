package io.safetynet.alerts.service;

import io.safetynet.alerts.api.dto.*;
import io.safetynet.alerts.api.mapper.FireStationMapper;
import io.safetynet.alerts.model.FireStation;
import io.safetynet.alerts.model.MedicalRecord;
import io.safetynet.alerts.model.Person;
import io.safetynet.alerts.repository.FireStationRepository;
import io.safetynet.alerts.repository.MedicalRecordRepository;
import io.safetynet.alerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
        if (fireStationAddresses.isEmpty()) {
            throw new RuntimeException("fireStation not exist");
        }
        List<Person> persons = personRepository.findByAddress(fireStationAddresses);
        List<MedicalRecord> medicalRecords = persons.stream().map(person ->
             medicalRecordRepository.find(person.getFirstName(), person.getLastName()).orElse(null))
                .filter(Objects::nonNull).toList();

        int children = medicalRecords.stream().filter(medicalRecord -> {
            LocalDate dateNow = LocalDate.now();
            LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
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

    public List<String> getPhoneNumbersByStation(String station) {
        List<String> addresses = fireStationRepository.findByStation(station);
        if (addresses.isEmpty()) {
            throw new RuntimeException("station not exist");
        }
        return personRepository.findByAddress(addresses).stream().map(Person::getPhone).distinct().toList();
    }

    public FireDto getByAddress(String address) {
        FireStation fireStation = fireStationRepository.findByAddress(address);
        if (fireStation == null) {
            throw new RuntimeException("Address station not exist");
        }
        List<Person> persons = personRepository.findByAddress(List.of(fireStation.getAddress()));

        List<PersonFireDto> personsFireDto= persons.stream().map(person -> {
            MedicalRecord medicalRecord = medicalRecordRepository.find(person.getFirstName(), person.getLastName()).orElse(null);
            if (medicalRecord == null) {
                return null;
            }
            LocalDate dateNow = LocalDate.now();
            LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            int age = Period.between(birthDate, dateNow).getYears();

            return new PersonFireDto(person.getLastName(), person.getPhone(), age, medicalRecord.getMedications() , medicalRecord.getAllergies());
        }).filter(Objects::nonNull).toList();
        return new FireDto(fireStation.getStation(),personsFireDto);
    }

    public Map<String, List<PersonFireDto>> getFlood(List<String> stations) {
        List<String> addresses = fireStationRepository.findByStations(stations);
        if (addresses.isEmpty()) {
            throw new RuntimeException("stations not exist");
        }
        List<Person> persons = personRepository.findByAddress(addresses);

        return persons.stream().collect(
            Collectors.groupingBy(Person::getAddress,Collectors.mapping(
                person -> {
                    MedicalRecord medicalRecord = medicalRecordRepository.find(person.getFirstName(),person.getLastName()).orElse(null);
                    if (medicalRecord == null) {
                        return null;
                    }
                    LocalDate dateNow = LocalDate.now();
                    LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    int age = Period.between(birthDate, dateNow).getYears();

                    return new PersonFireDto(person.getLastName(), person.getPhone(), age, medicalRecord.getMedications(), medicalRecord.getAllergies());
                },Collectors.toList())
            )
        );
    }
}
