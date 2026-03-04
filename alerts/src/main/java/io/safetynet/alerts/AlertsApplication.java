package io.safetynet.alerts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tools.jackson.databind.ObjectMapper;

@SpringBootApplication
public class AlertsApplication {

        private static final Logger logger = LoggerFactory.getLogger(AlertsApplication.class);



    public static void main(String[] args) {
        SpringApplication.run(AlertsApplication.class, args);
        logger.info("Application start");
        ObjectMapper mapper = new ObjectMapper();
//        Data data = mapper.readValue(new File("src/main/java/io/safetynet/alerts/data.json"),);
	}

}
