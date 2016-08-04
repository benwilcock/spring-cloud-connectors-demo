package io.benwilcock;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

/**
 * Created by benwilcock on 03/08/2016.
 */
@RestController
public class StatusController {

    @Autowired(required = false)
    DataSource dataSource;

    @Autowired(required = false)
    ConnectionFactory rabbitCf;

    @RequestMapping("/")
    public Status index(){
        return new Status(dataSource, rabbitCf);
    }
}
