package io.safetynet.alerts.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Data
public class MedicalRecord implements IdentifiedEntity {
    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;

    public int getAge(){
        LocalDate dateNow = LocalDate.now();
        LocalDate birthDate = LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        return Period.between(birthDate, dateNow).getYears();
    }


    public boolean isMajor(){
        return getAge() > 18;
    }

    public boolean isMinor(){
        return !isMajor();
    }


}
