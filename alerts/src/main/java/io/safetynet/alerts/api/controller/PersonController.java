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

    @GetMapping("/childAlert")
    public ResponseEntity<?> childAlert(@RequestParam String address) {
        try {
            List<ChildAlertDto> result = service.getChildrenByAddress(address);
            log.info("ChildAlert address : {}", address);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error(
                    "Error childAlert {}",
                    address
            );
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/personInfo")
    public ResponseEntity<?> getPersonInfo(@RequestParam String lastName) {
        try{
            List<PersonInfoWithMedicationDto> result = service.getPersonByLastName(lastName);
            log.info(
                    "PersonInfo with lastname {}",
                    lastName
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error(
                    "Error personInfo {}",
                    lastName
            );
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<?> getCommunityEmail(@RequestParam String city) {
        try {
            List<String> result = service.getEmailsByCity(city);
            log.info(
                    "Emails with city {}",
                    city
            );
            return ResponseEntity.ok(result);
        }catch (Exception e){
            log.error(
                    "Error Emails with city {} : {}",
                    city,
                    e.getMessage()
            );
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/person")
    public ResponseEntity<?> create(@RequestBody PersonDto personDto) {
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
    public ResponseEntity<?> update(@RequestBody PersonDto personDto) {
        try {
            PersonDto result = service.update(personDto);
            log.info(
                    "Person updated {} {}",
                    personDto.getFirstName(),
                    personDto.getLastName()
            );
            return ResponseEntity.status(HttpStatus.OK).body(result.getFirstName() + " " + result.getLastName());
        } catch (Exception e) {
            log.error(
                    "Error updating  person {} {} : {}",
                    personDto.getFirstName(),
                    personDto.getLastName(),
                    e.getMessage()
            );
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/person")
    public ResponseEntity<?> delete(@RequestBody PersonDto personDto) {
        try {
            service.delete(personDto);
            log.info(
                    "Person deleted {} {}",
                    personDto.getFirstName(),
                    personDto.getLastName()
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(
                    "Error deleting person {} {} : {}",
                    personDto.getFirstName(),
                    personDto.getLastName(),
                    e.getMessage()
            );
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
