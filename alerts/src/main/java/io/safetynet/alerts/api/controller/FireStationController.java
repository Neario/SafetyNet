package io.safetynet.alerts.api.controller;

import io.safetynet.alerts.api.dto.FireDto;
import io.safetynet.alerts.api.dto.FireStationDto;
import io.safetynet.alerts.api.dto.FireStationPersonInfoDto;
import io.safetynet.alerts.service.FireStationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RestController for FireStation endPoints
 */
@Log4j2
@RestController
@RequiredArgsConstructor
public class FireStationController {
    private final FireStationService fireStationService;

    /**
     * Return  all FireStations
     * @return list of station
     */
    @GetMapping("/firestations")
    public List<FireStationDto> index() {
        return fireStationService.findAll();
    }


    /**
     * Return a FireStation
     * @param station station number
     * @return FireStation asked
     */
    @GetMapping("/firestation")
    public ResponseEntity<FireStationPersonInfoDto> getByStation(@RequestParam String station) {
        return ResponseEntity.ok(fireStationService.getPersonsByStation(station));
    }

    /**
     * Create a FireStation
     * @param fireStationDto station data (address and number)
     * @return FireStation created
     */
    @PostMapping("/firestation")
    public ResponseEntity<FireStationDto> create(@RequestBody FireStationDto fireStationDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fireStationService.create(fireStationDto));

    }

    /**
     * Update FireStation
     * @param fireStationDto FireStation data(address with new number)
     * @return FireStation updated
     */
    @PutMapping("/firestation")
    public ResponseEntity<FireStationDto> update(@RequestBody FireStationDto fireStationDto) {
        return ResponseEntity.status(HttpStatus.OK).body(fireStationService.update(fireStationDto));
    }

    /**
     * Delete FireStation
     * @param fireStationDto FireStation data (address)
     * @return Response HTTP
     */
    @DeleteMapping("/firestation")
    public ResponseEntity<FireStationDto> delete(@RequestBody FireStationDto fireStationDto) {
        fireStationService.delete(fireStationDto);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * Return all phone number for all persons affiliate to station number
     * @param station station number
     * @return Request with a list of phone number for all persons affiliate to station number
     */
    @GetMapping("/phoneAlert")
    public ResponseEntity<List<String>> getByPhone(@RequestParam String station) {
        return ResponseEntity.ok(fireStationService.getPhoneNumbersByStation(station));
    }

    /**
     * Return all persons and station number affiliate to FireStation address
     * @param address FireStation address
     * @return Request with a list of person and station number affiliate to FireStation address
     */
    @GetMapping("/fire")
    public ResponseEntity<FireDto> getByAddress(@RequestParam String address) {
        return ResponseEntity.ok(fireStationService.getByAddress(address));
    }

    /**
     * Return all persons grouping by address affiliate to list of the station
     * @param stations list of station number
     * @return list of person groupBy address affiliate to list of the station
     */
    @GetMapping("/flood/stations")
    public ResponseEntity<?> flood(@RequestParam List<String> stations) {
        return ResponseEntity.ok(fireStationService.getFlood(stations));
    }
}
