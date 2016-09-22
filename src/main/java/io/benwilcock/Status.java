package io.benwilcock;

import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by benwilcock on 04/08/2016.
 */

@Component
public class Status {

    @Autowired
    DataSource dataSource;

    @Autowired
    ConnectionFactory rabbitConnectionFactory;

    @Autowired
    MongoDbFactory mongoDbFactory;

    private static final String id = UUID.randomUUID().toString();

    public Status() {
    }

    public String getId() {
        return id;
    }

    public String getSql() {
        String sql;

        if (dataSource == null) {
            sql = "<null>";
        } else {
            try {
                Field urlField = ReflectionUtils.findField(dataSource.getClass(), "url");
                ReflectionUtils.makeAccessible(urlField);
                sql = (String) urlField.get(dataSource);
            } catch (Exception fe) {
                try {
                    Method urlMethod = ReflectionUtils.findMethod(dataSource.getClass(), "getUrl");
                    ReflectionUtils.makeAccessible(urlMethod);
                    sql = (String) urlMethod.invoke(dataSource, (Object[])null);
                } catch (Exception me){
                    sql = "<unknown> " + dataSource.getClass();
                }
            }
        }
        return sql;
    }

    public boolean isSql(){
        return (null == dataSource);
    }

    public String getRabbit() {
        StringBuilder sb = new StringBuilder();

        if(rabbitConnectionFactory == null){
            sb.append("<null>");
        } else {
            sb.append(rabbitConnectionFactory.getHost());
            sb.append(":");
            sb.append(rabbitConnectionFactory.getPort());

            try {
                rabbitConnectionFactory.createConnection().isOpen();
                sb.append(":UP");
            } catch (AmqpConnectException ce){
                sb.append(":DOWN - ");
                sb.append(ce.getCause().getMessage());
            }
        }
        return sb.toString();
    }

    public boolean isRabbit(){
        return (null == rabbitConnectionFactory);
    }

    public String getMongo(){
        StringBuilder sb = new StringBuilder();

        if(mongoDbFactory == null){
            sb.append("<null>");
        } else {
            sb.append(mongoDbFactory.getDb().getMongo().getAddress());
        }
        return sb.toString();
    }

    public boolean isMongo(){
        return (null == mongoDbFactory);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getSql());
        sb.append("/n");
        sb.append(getRabbit());
        sb.append("/n");
        return "";
    }
}
