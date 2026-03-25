package io.safetynet.alerts.api.dto;

import io.safetynet.alerts.model.Person;

public record PersonInfoDto (
     String firstName,
     String lastName,
     String address,
     String phone
) {
    public PersonInfoDto(Person person) {
        this(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone());
    }
}
