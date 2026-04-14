package io.safetynet.alerts.service;

import io.safetynet.alerts.api.exception.AlreadyExistsException;
import io.safetynet.alerts.api.exception.NotFoundException;
import io.safetynet.alerts.api.dto.ChildAlertDto;
import io.safetynet.alerts.api.dto.PersonDto;
import io.safetynet.alerts.api.dto.PersonInfoWithMedicationDto;
import io.safetynet.alerts.api.mapper.PersonMapper;
import io.safetynet.alerts.model.IdentifiedEntity;
import io.safetynet.alerts.model.MedicalRecord;
import io.safetynet.alerts.model.Person;
import io.safetynet.alerts.repository.MedicalRecordRepository;
import io.safetynet.alerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Service for CRUD opérations Person and queries information
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class PersonService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PersonRepository repository;
    private final PersonMapper mapper;

    /**
     * Returns all Persons
     * @return list of PersonDto
     */
    public List<PersonDto> findAll() {
        return repository.findAll()
                .stream()
                .map(PersonDto::new)
                .toList();
    }

    /**
     * Create a new Person
     * @param personDto Person Data
     * @return created Person
     * @throws AlreadyExistsException if Person already exists
     */
    public PersonDto create(PersonDto personDto) {
        boolean exists = repository.findById(personDto.getId()).isPresent();
        if (exists) {
            throw new AlreadyExistsException("Person already exists");
        }
        Person person = mapper.fromDto(personDto);
        repository.create(person);
        log.info(
                "Person created {}",
                person.getId()
        );
        return personDto;
    }

    /**
     * Update a new Person
     * @param personDto Person Data
     * @return updated Person
     * @throws NotFoundException if Person not found
     */
    public PersonDto update(PersonDto personDto) {
        Person person = repository.findById(personDto.getId())
                .orElseThrow(() -> new NotFoundException("Person not exist"));
        mapper.update(person, personDto);
        repository.update(person);
        log.info(
                "Person updated {}",
                person.getId()
        );
        return personDto;
    }

    /**
     * Delete a Person
     * @param personDto Person Data
     * @throws NotFoundException if Person not found
     */
    public void delete(PersonDto personDto) {
        Person person = repository.findById(personDto.getId())
                .orElseThrow(() -> new NotFoundException("Person not exist"));
        log.info(
                "Person deleted {}",
                personDto.getId()
        );
        repository.delete(person);
    }

    /**
     * Returns minors persons and list of other person in same household
     * @param address address household
     * @return List of minors and list of other person in household or empty list
     */
    public List<ChildAlertDto> getChildrenByAddress(String address) {
        List<Person> persons = repository.findByAddress(List.of(address));
        List<MedicalRecord> medicalRecords = persons.stream()
                .map(person ->
                        medicalRecordRepository.findById(person.getId()).orElse(null)
                ).filter(Objects::nonNull).toList();

        log.info("ChildAlert address : {}", address);

        return medicalRecords.stream().map(medicalRecord -> {
            if (medicalRecord.isMajor()) {
                return null;
            }

            List<String> otherFamily = persons.stream()
                    .map(IdentifiedEntity::getId).filter(id -> !(id.equals(medicalRecord.getId()))
                    ).toList();

            return new ChildAlertDto(medicalRecord, otherFamily);

        }).filter(Objects::nonNull).toList();
    }

    /**
     * Returns persons and medical record with same lastname
     * @param lastName lastname person
     * @return List of person with medical record
     * @throws NotFoundException if persons with lastname not found
     */
    public List<PersonInfoWithMedicationDto> getPersonByLastName(String lastName) {
        List<Person> persons = repository.findByLastName(lastName);
        List<PersonInfoWithMedicationDto> personsInfoWithMedicationDto =  persons.stream().map(person -> {
            MedicalRecord medicalRecord = medicalRecordRepository.findById(person.getId()).orElse(null);
            if (medicalRecord == null) {
                return null;
            }
            return new PersonInfoWithMedicationDto(medicalRecord , person.getAddress(), person.getEmail());
        }).filter(Objects::nonNull).toList();
        if (personsInfoWithMedicationDto.isEmpty()) {
            throw new NotFoundException("Person with lastname : " + lastName + " not found");
        }
        log.info(
                "PersonInfo with lastname {}",
                lastName
        );
        return personsInfoWithMedicationDto;
    }

    /**
     * Returns emails from person living in the same city
     * @param city city person
     * @return list of email's person or empty list
     */
    public List<String> getEmailsByCity(String city) {
        List<Person> persons = repository.findByCity(city);
        log.info(
                "Emails with city {}",
                city
        );
        return persons.stream().map(Person::getEmail).toList();
    }
}

