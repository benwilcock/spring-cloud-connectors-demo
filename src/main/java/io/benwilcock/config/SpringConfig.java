package io.benwilcock.config;

import io.benwilcock.Status;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by benwilcock on 09/09/2016.
 */
@Configuration
public class SpringConfig {

    @Bean
    public Status status(DataSource dataSource, ConnectionFactory rabbitFactory){
        return new Status(dataSource, rabbitFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory rabbitFactory) {
        return new RabbitTemplate(rabbitFactory);
    }
}
