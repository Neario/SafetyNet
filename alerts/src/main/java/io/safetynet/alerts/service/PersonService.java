package io.safetynet.alerts.service;

import io.safetynet.alerts.api.dto.PersonDto;
import io.safetynet.alerts.api.mapper.PersonMapper;
import io.safetynet.alerts.model.Person;
import io.safetynet.alerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository repository;
    private final PersonMapper mapper;

    public List<PersonDto> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public PersonDto create(PersonDto personDto) {
        boolean exists = repository.find(personDto.getFirstName(), personDto.getLastName()).isPresent();
        if (exists) {
            throw new RuntimeException("Person already exists");
        }
        Person person = mapper.fromDto(personDto);
        repository.create(person);
        return mapper.toDto(person);
    }

    public PersonDto update(PersonDto personDto) {
        Person person = repository.find(
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
        Person person = repository.find(
                personDto.getFirstName(),
                personDto.getLastName()
        ).orElseThrow(
                () -> new RuntimeException("Person not exist")
        );
        repository.delete(person);
    }
}

