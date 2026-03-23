package io.safetynet.alerts.model;

import lombok.Data;

@Data
public class Person implements IdentifiedEntity {

    private String lastName;
    private String firstName;
    private String address;
    private String city;
    private String phone;
    private String email;
}
