package io.safetynet.alerts.api.controller;

import io.safetynet.alerts.api.dto.FireStationDto;
import io.safetynet.alerts.api.dto.PersonDto;
import io.safetynet.alerts.service.FireStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FireStationController {
    private final FireStationService fireStationService;

    @GetMapping("/firestations")
    public List<FireStationDto> index() {
        return fireStationService.findAll();
    }

    @PostMapping("/firestation")
    public FireStationDto create(@RequestBody FireStationDto fireStationDto) {
        return fireStationService.create(fireStationDto);
    }

    @PutMapping("/firestation")
    public FireStationDto update(@RequestBody FireStationDto fireStationDto) {
        return fireStationService.update(fireStationDto);
    }

    @DeleteMapping("/firestation")
    public void delete(@RequestBody FireStationDto fireStationDto) {
        fireStationService.delete(fireStationDto);
    }
}
