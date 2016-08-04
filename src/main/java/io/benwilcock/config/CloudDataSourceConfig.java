package io.benwilcock.config;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * Created by benwilcock on 03/08/2016.
 */
@Profile("cloud")
public class CloudDataSourceConfig extends AbstractCloudConfig {

    @Bean
    public DataSource inventoryDataSource() {
        return connectionFactory().dataSource();
    }
}
