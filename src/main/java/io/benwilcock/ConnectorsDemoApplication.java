package io.benwilcock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class ConnectorsDemoApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ConnectorsDemoApplication.class, args);
	}
}
