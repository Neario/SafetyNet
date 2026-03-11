package io.safetynet.alerts.api.controller;

import io.safetynet.alerts.api.dto.PersonDto;
import io.safetynet.alerts.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonService service;


    @GetMapping("/persons")
    public List<PersonDto> index() {
        return service.findAll();
    }

    @PostMapping("/person")
    public ResponseEntity<String> create(@RequestBody PersonDto personDto) {
        try {
            PersonDto result = service.create(personDto);
            log.info(
                    "Person created {} {}",
                    personDto.getFirstName(),
                    personDto.getLastName()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(result.getFirstName() + " " + result.getLastName());
        } catch (Exception e) {
            log.error(
                    "Error creating person {} {} : {}",
                    personDto.getFirstName(),
                    personDto.getLastName(),
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Person already exists");
        }

    }

    @PutMapping("/person")
    public PersonDto update(@RequestBody PersonDto personDto) {
        return service.update(personDto);
    }

    @DeleteMapping("/person")
    public void delete(@RequestBody PersonDto personDto) {
        service.delete(personDto);
    }
}
