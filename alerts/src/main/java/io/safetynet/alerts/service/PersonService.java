package io.safetynet.alerts.service;

import io.safetynet.alerts.api.dto.ChildAlertDto;
import io.safetynet.alerts.api.dto.PersonDto;
import io.safetynet.alerts.api.dto.PersonInfoWithMedicationDto;
import io.safetynet.alerts.api.mapper.PersonMapper;
import io.safetynet.alerts.model.MedicalRecord;
import io.safetynet.alerts.model.Person;
import io.safetynet.alerts.repository.MedicalRecordRepository;
import io.safetynet.alerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class PersonService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PersonRepository repository;
    private final PersonMapper mapper;

    public List<PersonDto> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public PersonDto create(PersonDto personDto) {
        boolean exists = repository.findByFirstNameAndLastName(personDto.getFirstName(), personDto.getLastName()).isPresent();
        if (exists) {
            throw new RuntimeException("Person already exists");
        }
        Person person = mapper.fromDto(personDto);
        repository.create(person);
        return mapper.toDto(person);
    }

    public PersonDto update(PersonDto personDto) {
        Person person = repository.findByFirstNameAndLastName(
                        personDto.getFirstName(),
                        personDto.getLastName()
                ).orElseThrow(
                        () -> new RuntimeException("Person not exist")
                );
        mapper.update(person, personDto);
        repository.update(person);
        return mapper.toDto(person);
    }

    public void delete(PersonDto personDto) {
        Person person = repository.findByFirstNameAndLastName(
                personDto.getFirstName(),
                personDto.getLastName()
        ).orElseThrow(
                () -> new RuntimeException("Person not exist")
        );
        repository.delete(person);
    }

    public List<ChildAlertDto> getChildrenByAddress(String address) {
        List<Person> persons = repository.findByAddress(List.of(address));
        List<MedicalRecord> medicalRecords = persons.stream().map(person ->
                        medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName()).orElse(null))
                .filter(Objects::nonNull).toList();

        return medicalRecords.stream().map(medicalRecord -> {
            LocalDate dateNow = LocalDate.now();
            LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            int age = Period.between(birthDate, dateNow).getYears();
            if (age > 18) {
                return null;
            }

            List<String> otherFamily = persons.stream().filter(person ->
                    !(person.getFirstName().equals(medicalRecord.getFirstName())
                            && person.getLastName().equals(medicalRecord.getLastName()))
            ).map(person -> person.getFirstName() + "," + person.getLastName()).toList();
            return new ChildAlertDto(medicalRecord.getFirstName(), medicalRecord.getLastName(), age, otherFamily);
        }).filter(Objects::nonNull).toList();
    }

    public List<PersonInfoWithMedicationDto> getPersonByLastName(String lastName) {
        List<Person> persons = repository.findByLastName(lastName);
        List<PersonInfoWithMedicationDto> personsInfoWithMedicationDto =  persons.stream().map(person -> {
            MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(),person.getLastName()).orElse(null);
            if (medicalRecord == null) {
                return null;
            }
            LocalDate dateNow = LocalDate.now();
            LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            int age = Period.between(birthDate, dateNow).getYears();

            return new PersonInfoWithMedicationDto(person.getLastName(),person.getAddress(),age, person.getEmail(), medicalRecord.getMedications(), medicalRecord.getAllergies());
        }).filter(Objects::nonNull).toList();
        if (personsInfoWithMedicationDto.isEmpty()) {
            throw new RuntimeException("Person with lastname : " + lastName + " not found");
        }
        return personsInfoWithMedicationDto;
    }

    public List<String> getEmailsByCity(String city) {
        List<Person> persons = repository.findByCity(city);
        // todo : si pas de mails trouvés , retourne une liste vide
//        if (persons.isEmpty()) {
//            throw new RuntimeException("Person with city : " + city + " not found");
//        }
        return persons.stream().map(Person::getEmail).toList();
    }
}

