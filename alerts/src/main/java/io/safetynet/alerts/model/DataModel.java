package io.safetynet.alerts.model;

import lombok.Data;

import java.util.List;

@Data
public class DataModel {

    List<Person> persons;
    List<FireStation> firestations;
    List<MedicalRecord> medicalrecords;

}
