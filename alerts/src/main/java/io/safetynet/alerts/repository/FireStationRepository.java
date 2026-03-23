package io.safetynet.alerts.repository;

import io.safetynet.alerts.api.Exception.AlreadyExistsException;
import io.safetynet.alerts.model.FireStation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Repository for FireStation data
 * Access to FireStations stored in JSON database
 */
@Repository
@RequiredArgsConstructor
public class FireStationRepository {

    private final DatabaseLoader databaseLoader;

    /**
     * Returns all FireStations
     * @return List of FireStation
     */
    public List<FireStation> findAll() {
        return databaseLoader.getDatas().getFirestations();
    }

    /**
     * Return a FireStation by address
     * @param address address of FireStation
     * @return Optional FireStation
     */
    public Optional<FireStation> find(String address) {
        return findAll().stream().filter(fireStation ->
                        fireStation.getAddress().equals(address))
                .findFirst();
    }

    /**
     * Return a FireStation by a number station
     * @param station station number
     * @return List of address
     */
    public List<String> findByStation(String station) {
        return databaseLoader.getDatas().getFirestations().stream().filter(fireStation ->
                Objects.equals(fireStation.getStation(), station)).map(FireStation::getAddress).toList();
    }

    /**
     * Return a FireStation by address
     * @param address address of FireStation
     * @return Optional FireStation
     */
    public Optional<FireStation> findByAddress(String address) {
        return databaseLoader.getDatas().getFirestations().stream().filter(fireStation ->
                fireStation.getAddress().equals(address)).findFirst();
    }

    /**
     * Return list of address affiliated to stations
     * @param stations stations list of number FireStation
     * @return
     */
    public List<String> findByStations(List<String> stations) {
        return databaseLoader.getDatas().getFirestations().stream().filter(
                fireStation ->stations.contains(fireStation.getStation())
        ).map(FireStation::getAddress).toList();
    }

    /**
     * Create a FireStation
     * @param fireStation FireStation Data
     * @return created FireStation
     */
    public FireStation create(FireStation fireStation) {
        boolean exists = find(fireStation.getAddress()).isPresent();
        if (exists) {
            throw new AlreadyExistsException("FireStation with address " + fireStation.getAddress() + " already exists");
        }
        findAll().add(fireStation);
        databaseLoader.save();
        return fireStation;
    }

    /**
     * Update a FireStation
     * @param fireStation FireStation Data
     * @return updated FireStation
     */
    public FireStation update(FireStation fireStation) {
        databaseLoader.save();
        return fireStation;
    }

    /**
     * Delete a FireStation
     * @param fireStation FireStation Data
     */
    public void delete(FireStation fireStation) {
        databaseLoader.getDatas().getFirestations().remove(fireStation);
        databaseLoader.save();
    }
}
