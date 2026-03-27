package io.safetynet.alerts.model;

public interface IdentifiedEntity {

    String getFirstName();

    String getLastName();

    default String getId() {
        return getFirstName() + "-" + getLastName();
    }

}
