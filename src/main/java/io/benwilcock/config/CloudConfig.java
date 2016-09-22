package io.benwilcock.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;

import javax.sql.DataSource;

/**
 * Created by benwilcock on 07/09/2016.
 */
public class CloudConfig extends AbstractCloudConfig {

    //Connect to the only available database service
    @Bean
    public DataSource dataSource() {
        return connectionFactory().dataSource();
    }

    //Connect to the only available RabbitMQ service
    @Bean
    public ConnectionFactory rabbitFactory() {
        return connectionFactory().rabbitConnectionFactory();
    }

    //Connect to the only available MongoDB service
    @Bean
    public MongoDbFactory mongoFactory() {
        return connectionFactory().mongoDbFactory();
    }
}
