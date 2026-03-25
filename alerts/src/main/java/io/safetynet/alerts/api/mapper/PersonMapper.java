package io.safetynet.alerts.api.mapper;

import io.safetynet.alerts.api.dto.PersonDto;
import io.safetynet.alerts.model.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public Person fromDto(PersonDto personDto) {

        Person person = new Person();

        person.setFirstName(personDto.firstName());
        person.setLastName(personDto.lastName());
        person.setEmail(personDto.email());
        person.setAddress(personDto.address());
        person.setCity(personDto.city());
        person.setPhone(personDto.phone());

        return person;
    }

    public void update(Person person, PersonDto personDto) {

        person.setEmail(personDto.email());
        person.setAddress(personDto.address());
        person.setCity(personDto.city());
        person.setPhone(personDto.phone());
    }
}
