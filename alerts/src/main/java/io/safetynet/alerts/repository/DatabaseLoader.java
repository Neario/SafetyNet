package io.safetynet.alerts.repository;


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

    @Getter
    private final DataModel datas;

    public DatabaseLoader(ObjectMapper objectMapper ) throws IOException {

        final ResourceLoader resourceLoader = new DefaultResourceLoader();
        String dataPath = "data.json";
        final Resource resource = resourceLoader.getResource(dataPath);
        datas = objectMapper.readValue(resource.getInputStream(), DataModel.class);
    }
}
