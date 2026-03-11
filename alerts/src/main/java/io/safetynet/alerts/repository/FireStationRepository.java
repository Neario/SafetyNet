package io.safetynet.alerts.repository;

import io.safetynet.alerts.model.FireStation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FireStationRepository {

    private final DatabaseLoader databaseLoader;

    public List<FireStation> findAll() {
        return databaseLoader.getDatas().getFirestations();
    }

    public Optional<FireStation> find(String address) {
        return findAll().stream().filter(fireStation ->
                        fireStation.getAddress().equals(address))
                .findFirst();
    }

    public FireStation create(FireStation fireStation) {
        boolean exists = find(fireStation.getAddress()).isPresent();
        if (exists) {
            throw new RuntimeException("FireStation already exists");
        }
        findAll().add(fireStation);
        databaseLoader.save();
        return fireStation;
    }

    public FireStation update(FireStation fireStation) {
        databaseLoader.save();
        return fireStation;
    }

    public void delete(FireStation fireStation) {
        databaseLoader.getDatas().getFirestations().remove(fireStation);
        databaseLoader.save();
    }
}
