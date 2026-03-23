package io.safetynet.alerts.repository;

import io.safetynet.alerts.model.MedicalRecord;
import io.safetynet.alerts.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PersonRepository {

    private final DatabaseLoader databaseLoader;

    public List<Person> findAll() {
        return databaseLoader.getDatas().getPersons();
    }

    public Optional<Person> findByFirstNameAndLastName(String firstName, String lastName) {
        return findAll().stream().filter(person ->
                person.getFirstName().equals(firstName) && person.getLastName().equals(lastName))
                .findFirst();
    }

    public Optional<Person> findById(String id) {
        return findAll().stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public List<Person> findByAddress(String address) {
        return databaseLoader.getDatas().getPersons().stream().filter(person -> person.getAddress().equals(address)).toList();
    }


    public List<Person> findByAddress(List<String> datas) {
        return databaseLoader.getDatas().getPersons().stream().filter(person ->
                datas.contains(person.getAddress())).toList();
    }

    public List<Person> findByLastName(String lastName) {
        return databaseLoader.getDatas().getPersons().stream().filter(person ->
                lastName.equals(person.getLastName())).toList();
    }

    public List<Person> findByCity(String city) {
        return databaseLoader.getDatas().getPersons().stream().filter(person ->
                city.equals(person.getCity())).toList();
    }

    public Person create(Person person) {
        findAll().add(person);
        databaseLoader.save();
        return person;
    }

    public Person update(Person person) {
        databaseLoader.save();
        return person;
    }

    public void delete(Person person) {
        databaseLoader.getDatas().getPersons().remove(person);
        databaseLoader.save();
    }
}
