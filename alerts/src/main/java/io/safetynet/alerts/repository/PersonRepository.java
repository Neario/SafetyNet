package io.safetynet.alerts.repository;

import io.safetynet.alerts.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PersonRepository {

    private final DatabaseLoader databaseLoader;

    public List<Person> findAll() {
        return databaseLoader.getDatas().getPersons();
    }

}
