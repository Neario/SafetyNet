package io.safetynet.alerts.repository;

import io.safetynet.alerts.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Person data
 * Access to Person stored in JSON database
 */
@Repository
@RequiredArgsConstructor
public class PersonRepository {

    private final DatabaseLoader databaseLoader;

    /**
     * Returns all Persons
     * @return List of Person
     */
    public List<Person> findAll() {
        return databaseLoader.getDatas().getPersons();
    }

    public Optional<Person> findByFirstNameAndLastName(String firstName, String lastName) {
        return findAll().stream().filter(person ->
                person.getFirstName().equals(firstName) && person.getLastName().equals(lastName))
                .findFirst();
    }

    /**
     * Return a Person by id
     * @param id id of Person (firstName, lastName)
     * @return Optional Person
     */
    public Optional<Person> findById(String id) {
        return findAll().stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    /**
     * Return Persons by address
     * @param address address of FireStation
     * @return List of person
     */
    public List<Person> findByAddress(String address) {
        return databaseLoader.getDatas().getPersons().stream().filter(person -> person.getAddress().equals(address)).toList();
    }

    /**
     * Return Persons by addresses
     * @param datas addresses of FireStation
     * @return List of person
     */
    public List<Person> findByAddress(List<String> datas) {
        return databaseLoader.getDatas().getPersons().stream().filter(person ->
                datas.contains(person.getAddress())).toList();
    }

    /**
     * Return Persons by lastName
     * @param lastName lastName Person
     * @return List of person
     */
    public List<Person> findByLastName(String lastName) {
        return databaseLoader.getDatas().getPersons().stream().filter(person ->
                lastName.equals(person.getLastName())).toList();
    }

    /**
     * Return Persons by city
     * @param city city Person
     * @return List of person
     */
    public List<Person> findByCity(String city) {
        return databaseLoader.getDatas().getPersons().stream().filter(person ->
                city.equals(person.getCity())).toList();
    }

    /**
     * Create a Person
     * @param person Person Data
     * @return created Person
     */
    public Person create(Person person) {
        findAll().add(person);
        databaseLoader.save();
        return person;
    }

    /**
     * Update a Person
     * @param person Person Data
     * @return updated Person
     */
    public Person update(Person person) {
        databaseLoader.save();
        return person;
    }

    /**
     * Delete a Person
     * @param person Person Data
     */
    public void delete(Person person) {
        databaseLoader.getDatas().getPersons().remove(person);
        databaseLoader.save();
    }
}
