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

    public DatabaseLoader(CustomProperties properties, ObjectMapper objectMapper ) throws IOException {
        this.properties = properties;
        this.objectMapper = objectMapper;

        final ResourceLoader resourceLoader = new DefaultResourceLoader();
        resource = resourceLoader.getResource(properties.getApiUrl());
        datas = objectMapper.readValue(resource.getInputStream(), DataModel.class);
    }

    public void save() {
        try {
            objectMapper.writeValue(resource.getFile(), datas);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
