package io.benwilcock.config;

import io.benwilcock.Status;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by benwilcock on 04/08/2016.
 */
@Configuration
public class CustomInfoEndpointAdditions {

    @Autowired(required = false)
    DataSource dataSource;

    @Autowired(required = false)
    ConnectionFactory rabbitCf;

    /** These properties will show up in the Spring Boot Actuator /info endpoint **/
    @Autowired
    public void setInfoProperties(ConfigurableEnvironment env) {

        //Get the status of the application as currently configured
        Status status = new Status(dataSource, rabbitCf);

        // Add the status to the /info endpoint using Properties
        Properties props = new Properties();
        props.put("info.id", status.getId());
        props.put("info.sql", status.getSql());
        props.put("info.rabbit", status.getRabbit());

        // Set the new properties into the environment
        env.getPropertySources().addFirst(new PropertiesPropertySource("extra-info-props", props));
    }
}
