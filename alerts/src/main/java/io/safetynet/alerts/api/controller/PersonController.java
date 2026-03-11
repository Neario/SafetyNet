package io.safetynet.alerts.api.controller;

import io.safetynet.alerts.api.dto.PersonDto;
import io.safetynet.alerts.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonService service;


    @GetMapping("/persons")
    public List<PersonDto> index() {
        return service.findAll();
    }

    @PostMapping("/person")
    public PersonDto create(@RequestBody PersonDto personDto) {
        return service.create(personDto);
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
