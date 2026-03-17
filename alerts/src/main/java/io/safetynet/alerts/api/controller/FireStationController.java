package io.safetynet.alerts.api.controller;

import io.safetynet.alerts.api.dto.FireStationDto;
import io.safetynet.alerts.api.dto.FireStationPersonInfoDto;
import io.safetynet.alerts.service.FireStationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class FireStationController {
    private final FireStationService fireStationService;

    @GetMapping("/firestations")
    public List<FireStationDto> index() {
        return fireStationService.findAll();
    }

    @GetMapping("/firestation")
    public ResponseEntity<?> getByStation(@RequestParam String station) {
        try{
            FireStationPersonInfoDto result = fireStationService.getPersonsByStation(station);
            log.info(
                    "Firestation {} returned {} persons",
                    station,
                    result.getPersons().size()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e){
            log.error(
                    "Error /firestation {} : {}",
                    station,
                    e.getMessage()
            );

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/firestation")
    public ResponseEntity<?> create(@RequestBody FireStationDto fireStationDto) {
        try{
            FireStationDto result = fireStationService.create(fireStationDto);
            log.info(
                    "FireStation created {}",
                    fireStationDto.getStation()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(result.getStation());
        }catch (Exception e) {
            log.error(
                    "Error created FireStation {}",
                    fireStationDto.getStation()
            );
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @PutMapping("/firestation")
    public ResponseEntity<?> update(@RequestBody FireStationDto fireStationDto) {
        try{
            FireStationDto result = fireStationService.update(fireStationDto);
            log.info(
                    "FireStation updated {}",
                    fireStationDto.getStation()
            );
            return ResponseEntity.status(HttpStatus.OK).body(result.getStation());
        }catch (Exception e) {
            log.error(
                    "Error updated FireStation {}",
                    fireStationDto.getStation()
            );
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/firestation")
    public ResponseEntity<?> delete(@RequestBody FireStationDto fireStationDto) {
        try {
            fireStationService.delete(fireStationDto);
            log.info(
                    "fireStation deleted {}",
                    fireStationDto.getStation()
            );
            return  ResponseEntity.status(HttpStatus.OK).body(fireStationDto.getStation());
        } catch (Exception e) {
            log.error(
                    "Error deleting fireStation {} : {}",
                    fireStationDto.getStation(),
                    e.getMessage()
            );
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
