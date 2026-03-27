package io.safetynet.alerts.repository;

import io.safetynet.alerts.config.CustomProperties;
import io.safetynet.alerts.model.DataModel;
import lombok.Getter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class DatabaseLoader {

    private final CustomProperties properties;
    private final ObjectMapper objectMapper;

    private final Resource resource;

    @Getter
    private final DataModel datas;

    public DatabaseLoader(CustomProperties properties, ObjectMapper objectMapper) throws IOException {
        this.properties = properties;
        this.objectMapper = objectMapper;

        final ResourceLoader resourceLoader = new DefaultResourceLoader();
        resource = resourceLoader.getResource(properties.getApiUrl());
        System.out.println("FILE = " + resource.getFile().getAbsolutePath());

        datas = objectMapper.readValue(resource.getInputStream(), DataModel.class);
    }

    public void save() {
        if (!properties.isEnableSave()) {
            return;
        }

        try {
            objectMapper.writeValue(resource.getFile(), datas);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reload() {
        try {
            DataModel newData =
                    objectMapper.readValue(resource.getInputStream(), DataModel.class);

            datas.getPersons().clear();
            datas.getPersons().addAll(newData.getPersons());

            datas.getFirestations().clear();
            datas.getFirestations().addAll(newData.getFirestations());

            datas.getMedicalrecords().clear();
            datas.getMedicalrecords().addAll(newData.getMedicalrecords());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
