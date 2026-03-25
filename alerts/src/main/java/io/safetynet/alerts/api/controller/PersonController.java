package io.safetynet.alerts.api.controller;

import io.safetynet.alerts.api.dto.ChildAlertDto;
import io.safetynet.alerts.api.dto.PersonDto;
import io.safetynet.alerts.api.dto.PersonInfoWithMedicationDto;
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

    /**
     * Return persons minor with a list of other members of their household
     * @param address address
     * @return list of minors with a list of other person in their household
     */
    @GetMapping("/childAlert")
    public ResponseEntity<List<ChildAlertDto>> childAlert(@RequestParam String address) {
        List<ChildAlertDto> result = service.getChildrenByAddress(address);
        return ResponseEntity.ok(result);
    }

    /**
     * Return persons with same lastname and their medical record
     * @param lastName lastName person
     * @return list of persons and their medical record
     */
    @GetMapping("/personInfo")
    public ResponseEntity<List<PersonInfoWithMedicationDto>> getPersonInfo(@RequestParam String lastName) {
        List<PersonInfoWithMedicationDto> result = service.getPersonByLastName(lastName);
        return ResponseEntity.ok(result);
    }

    /**
     * Return emails from person living in the same city
     * @param city city person
     * @return list of email's person in the same city
     */
    @GetMapping("/communityEmail")
    public ResponseEntity<?> getCommunityEmail(@RequestParam String city) {
        List<String> result = service.getEmailsByCity(city);
        return ResponseEntity.ok(result);
    }

    /**
     * Create a person
     * @param personDto person data (firstName, lastName, email, address, city, phone)
     * @return person created
     */
    @PostMapping("/person")
    public ResponseEntity<String> create(@RequestBody PersonDto personDto) {
        PersonDto result = service.create(personDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result.getId());
    }

    /**
     * Update a person
     * @param personDto person data
     * @return person updated
     */
    @PutMapping("/person")
    public ResponseEntity<String> update(@RequestBody PersonDto personDto) {
            PersonDto result = service.update(personDto);
            return ResponseEntity.status(HttpStatus.OK).body(result.getId());
    }

    /**
     * Delete a person
     * @param personDto person data
     * @return Http Response
     */
    @DeleteMapping("/person")
    public ResponseEntity<?> delete(@RequestBody PersonDto personDto) {
        service.delete(personDto);
        return ResponseEntity.ok().build();
    }
}
