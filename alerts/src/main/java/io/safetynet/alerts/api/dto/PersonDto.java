package io.safetynet.alerts.api.dto;

import io.safetynet.alerts.model.Person;

public record PersonDto (
     String firstName,
     String lastName,
     String email,
     String address,
     String city,
     String phone
     ) {

    public PersonDto (Person person) {
        this(
                person.getFirstName(),
                person.getLastName(),
                person.getEmail(),
                person.getAddress(),
                person.getCity(),
                person.getPhone()
        );
    }

    public String getId() {
        return firstName + "-" + lastName;
    }
}
