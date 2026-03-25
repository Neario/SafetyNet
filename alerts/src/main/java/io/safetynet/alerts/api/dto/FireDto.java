package io.safetynet.alerts.api.dto;

import java.util.List;

public record FireDto (
     String station,
     List<PersonFireDto> persons
){}
