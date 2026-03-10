package io.safetynet.alerts.api.mapper;

import io.safetynet.alerts.api.dto.PersonDto;
import io.safetynet.alerts.api.dto.PersonUpdateDto;
import io.safetynet.alerts.model.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public PersonDto toDto(Person person) {
        return PersonDto.builder()
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .email(person.getEmail())
                .address(person.getAddress())
                .city(person.getCity())
                .phone(person.getPhone())
                .build();
    }

    public Person fromDto(PersonDto personDto) {

        Person person = new Person();

        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        person.setEmail(personDto.getEmail());
        person.setAddress(personDto.getAddress());
        person.setCity(personDto.getCity());
        person.setPhone(personDto.getPhone());

        return person;
    }

    public void update(Person person, PersonUpdateDto personUpdateDto) {

        person.setEmail(personUpdateDto.getEmail());
        person.setAddress(personUpdateDto.getAddress());
        person.setCity(personUpdateDto.getCity());
        person.setPhone(personUpdateDto.getPhone());
    }
}
