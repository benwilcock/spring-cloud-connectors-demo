# spring-cloud-connectors-demo

This project demonstrates the cloud native capabilities of SpringBoot alongside the great developer experience of having local services. The code in this project shows you how to connect to services hosted either locally or as services in Cloud Foundry, all without any code or config changes required.

The services that you'll use for this demo are:-

- MongoDb
- RabbitMQ
- MySQL

## What you'll need

1. cf CLI to **push** apps to Cloud Foundry.
2. Docker to host your local services, or locally installed services.

## How it works

Spring cloud connectors is trained to look for connection details in the servers environment properties. When found, the connector will create a default spring Bean to use to work with the resource. Ib the case of a SQL database this would be a `javax.sql.DataSource`. For other resource types (Rabbit, MongoDB, etc) an appropriate connector is used.

For more information take a look at the documentation...

- [Spring Cloud Connectors (General)](http://cloud.spring.io/spring-cloud-connectors/spring-cloud-connectors.html).
- [Spring Cloud Connectors - Cloud Foundry Connector](http://cloud.spring.io/spring-cloud-connectors/spring-cloud-cloud-foundry-connector.html).
- [Spring Cloud Connectors - Local Connector](http://cloud.spring.io/spring-cloud-connectors/spring-cloud-connectors.html#_local_configuration_connector).
 
 The demo code is depending on the *Local Connector* and *Cloud Foundry Connector* as you can see in the `build.gradle`.

## To Run locally (no cloud required)

This demo requires all the above servers to be present and available on the machine you're developing on. The easiest way to do this is to use Docker as instructed below.

1. Start the local servers using docker-compose (requires Docker).

````bash
$ cd <your-git-folder-for-this-repo>
$ docker-compose up -d
````

2. Build the code.

````bash
$ ./gradlew assemble
````

3. Run the demo.

````bash
$ ./gradlew bootRun
````

> Notes: When working locally, JVM arguments are required to bootstrap the connectors. You can view these in the `build.gradle` configuration file.

````groovy
def JvmSettings = [
        "-Dspring.cloud.appId=sc-connectors-demo",
        "-Dspring.profiles.active=cloud",
        "-Dspring.cloud.database=mysql://user:pass@localhost:3306/test_db",
        "-Dspring.cloud.rabbitmq=amqp://user:pass@localhost:5672/test_vhost",
        "-Dspring.cloud.mongodb=mongodb://localhost:27017/test_db"
]
````

## Run on the Cloud

1. Login to Pivotal Web Services from the cf CLI

````bash
$ cd <your-git-folder-for-this-repo>
$ cf login -a api.run.pivotal.io
````

2. Provision the services that you'll need.

````bash
$ cf create-service cleardb spark mysql
$ cf create-service cloudamqp lemur rabbit
$ cf create-service mlab sandbox mongodb
````

3. Build the code.

````bash
$ ./gradlew assemble
````

4. Push the demo app to Cloud Foundry (no need to bind the services, it's automatic thanks to the `manifest.yml`).

````bash
$ cf push
````

> Notes: When working on Pivotal Cloud Foundry, when you bind services to the application, environment properties are used to provide the necessary connection properties. This even works in PCF-Dev if you update to the java_buildpack 3.7 or higher and add the necessary user provided services (via `cf cups`). The application manifest declares that the services must be available when deploying.
