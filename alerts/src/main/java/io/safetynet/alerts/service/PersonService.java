package io.safetynet.alerts.service;

import io.safetynet.alerts.api.dto.PersonDto;
import io.safetynet.alerts.api.mapper.PersonMapper;
import io.safetynet.alerts.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository repository;
    private final PersonMapper mapper;

    public List<PersonDto> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}

