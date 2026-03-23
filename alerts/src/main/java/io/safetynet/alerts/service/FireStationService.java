package io.safetynet.alerts.service;

import io.safetynet.alerts.api.Exception.AlreadyExistsException;
import io.safetynet.alerts.api.Exception.NotFoundException;
import io.safetynet.alerts.api.dto.*;
import io.safetynet.alerts.api.mapper.FireStationMapper;
import io.safetynet.alerts.model.FireStation;
import io.safetynet.alerts.model.MedicalRecord;
import io.safetynet.alerts.model.Person;
import io.safetynet.alerts.repository.FireStationRepository;
import io.safetynet.alerts.repository.MedicalRecordRepository;
import io.safetynet.alerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for CRUD opérations FireStation and queries information
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class FireStationService {

    private final FireStationRepository fireStationRepository;
    private final FireStationMapper fireStationMapper;
    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    /**
     * Returns all FireStations
     * @return list of FireStationDto
     */
    public List<FireStationDto> findAll()
    {
        return fireStationRepository.findAll()
                .stream().
                map(fireStationMapper::toDto)
                .toList();
    }

    /**
     * Create a new FireStation
     * @param fireStationDto FireStation Data
     * @return created FireStation
     * @throws AlreadyExistsException if FireStation already exists
     */
    public FireStationDto create(FireStationDto fireStationDto) {
        FireStation fireStation = fireStationMapper.fromDto(fireStationDto);
        fireStationRepository.create(fireStation);
        log.info(
                "FireStation Created {} : {}",
                fireStation.getAddress(),
                fireStationDto.getStation()
        );
        return fireStationMapper.toDto(fireStation);
    }

    /**
     * Update a FireStation
     * @param fireStationDto FireStation to update
     * @return updated FireStation
     * @throws NotFoundException if the FireStation not found
     */
    public FireStationDto update(FireStationDto fireStationDto) {
        FireStation fireStation =
                fireStationRepository
                        .find(fireStationDto.getAddress())
                        .orElseThrow(() -> new NotFoundException("FireStation not found")
        );
        fireStationMapper.update(fireStation, fireStationDto);
        fireStationRepository.update(fireStation);
        log.info(
                "FireStation Updated {} : {}",
                fireStation.getAddress(),
                fireStationDto.getStation()
        );
        return fireStationMapper.toDto(fireStation);
    }

    /**
     * Delete a FireStation
     * @param fireStationDto FireStation to delete
     * @throws NotFoundException if FireStation not found
     */
    public void delete(FireStationDto fireStationDto) {
        FireStation fireStation =
                fireStationRepository
                        .find(fireStationDto.getAddress())
                        .orElseThrow(() -> new NotFoundException("FireStation not found"));
        fireStationRepository.delete(fireStation);
        log.info(
                "fireStation deleted {}",
                fireStation.getAddress()
        );
    }

    /**
     * Returns persons affiliated with the station
     * @param station number station
     * @return persons with child and adult count
     */
    public FireStationPersonInfoDto getPersonsByStation(final String station) {
        List<String> fireStationAddresses = fireStationRepository.findByStation(station);

        List<Person> persons = personRepository.findByAddress(fireStationAddresses);

        Set<String> personIds = persons.stream().map(Person::getId).collect(Collectors.toSet());

        List<MedicalRecord> medicalRecords = medicalRecordRepository.findByIds(personIds);

        Assert.isTrue(persons.size() == medicalRecords.size(), "person and MedicalRecords don't match");

        final int adults = medicalRecords.stream().filter(MedicalRecord::isMajor).toList().size();

        final List<PersonInfoDto> personsDto = persons.stream().map(PersonInfoDto::new).toList();

        return new FireStationPersonInfoDto(personsDto, adults);
    }

    /**
     * Returns phone numbers of persons affiliated with FireStation
     * @param station number station
     * @return List of phone numbers
     * @throws NotFoundException if FireStation not found
     */
    public List<String> getPhoneNumbersByStation(final String station) {
        List<String> addresses =
                fireStationRepository
                        .findByStation(station);
        if (addresses.isEmpty()) {
            throw  new NotFoundException("station with number " + station + "not found");
        }
        log.info(
                "fireStation : {} get persons' phone number",
                station
        );
        return personRepository.findByAddress(addresses).stream().map(Person::getPhone).distinct().toList();
    }

    /**
     * Returns persons affiliated with FireStation
     * @param address address FireStation
     * @return persons with medical record with station number
     */
    public FireDto getByAddress(final String address) {
        final FireStation fireStation =
                fireStationRepository
                        .findByAddress(address)
                        .orElseThrow(() -> new NotFoundException("FireStation not found"));

        final List<PersonFireDto> personsFireDto = personRepository
                .findByAddress(fireStation.getAddress())
                .stream()
                .map(this::createPersonFireDto)
                .toList();

        log.info(
                "FireStation address {} returns the people affiliated with medical records",
                address
        );
        return new FireDto(fireStation.getStation(),personsFireDto);
    }

    /**
     * Returns list of persons in household groupBy address for stations
     * @param stations numbers of stations
     * @return persons with medical record groupBy address
     */
    public Map<String, List<PersonFireDto>> getFlood(final List<String> stations) {
        final Set<String> addresses = Set.copyOf(fireStationRepository.findByStations(stations));
        final List<Person> persons = personRepository.findByAddress(addresses.stream().toList());
        log.info(
                "FireStation flood {} return persons affiliated grouping by address",
                stations
        );
        return persons.stream().collect(
            Collectors.groupingBy(Person::getAddress,Collectors.mapping(
                    this::createPersonFireDto,Collectors.toList())
            )
        );
    }

    private PersonFireDto createPersonFireDto(final Person person) {
        final MedicalRecord medicalRecord = medicalRecordRepository.findById(person.getId())
                .orElseThrow(() -> new NotFoundException("MedicalRecord not found"));
        return new PersonFireDto(person, medicalRecord);
    }
}
