package io.safetynet.alerts.api.controller;

import io.safetynet.alerts.api.dto.PersonDto;
import io.safetynet.alerts.model.Person;
import io.safetynet.alerts.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonService service;


    @GetMapping("/persons")
    public List<PersonDto> index() {
        return service.findAll();
    }

}
