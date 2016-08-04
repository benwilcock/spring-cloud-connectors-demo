package io.benwilcock;

import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by benwilcock on 04/08/2016.
 */
public class Status {

    private String id = UUID.randomUUID().toString();
    private String sql;
    private String rabbit;

    public Status(DataSource dataSource, ConnectionFactory rabbitCf) {

        if (dataSource == null) {
            this.sql = "<null>";
        } else {
            try {
                Field urlField = ReflectionUtils.findField(dataSource.getClass(), "url");
                ReflectionUtils.makeAccessible(urlField);
                this.sql = (String) urlField.get(dataSource);
            } catch (Exception fe) {
                try {
                    Method urlMethod = ReflectionUtils.findMethod(dataSource.getClass(), "getUrl");
                    ReflectionUtils.makeAccessible(urlMethod);
                    this.sql = (String) urlMethod.invoke(dataSource, (Object[])null);
                } catch (Exception me){
                    this.sql = "<unknown> " + dataSource.getClass();
                }
            }
        }


        if(rabbitCf == null){
            this.rabbit = "<null>";
        } else {
            StringBuilder sb = new StringBuilder(rabbitCf.getHost());
            sb.append(":");
            sb.append(rabbitCf.getPort());

            try {
                rabbitCf.createConnection().isOpen();
                sb.append(":UP");
            } catch (AmqpConnectException ce){
                sb.append(":DOWN - ");
                sb.append(ce.getCause().getMessage());
            }
            this.rabbit = sb.toString();
        }
    }

    public String getId() {
        return id;
    }

    public String getSql() {
        return sql;
    }

    public String getRabbit() {
        return rabbit;
    }
}
