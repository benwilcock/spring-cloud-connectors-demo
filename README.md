# spring-cloud-connectors-demo

The code in this project demonstrates how to connect your app to backing services hosted either locally on your machine or as services in Cloud Foundry, all without any code or config changes being necessary.

This will demonstrate the [12 factor principles](http://12factor.net) of...

- storing config in the environment
- dev/prod parity
- backing services
- one codebase.

The backing services that you'll use for this demo are:-

- MongoDb
- RabbitMQ
- MySQL

If you follow along with the instructions below we'll first run the code on your local machine against the backing services in the list above, and then we'll run the same code remotely against a set of cloud hosted backing services _without changing the code or using multiple configuration files._

## What you'll need

1. A [Pivotal Web Services](http://run.pivotal.io) account (or PCF-Dev v0.20).
2. The [cf CLI](https://github.com/cloudfoundry/cli#downloads) ()in order to push apps to Pivotal Web Services).
3. [Docker](http://docker.com) to host the backing services locally (or locally installed backing services if you don't want to use Docker).
4. Java SDK 1.8
5. About 15 minutes of your time.

## To Run locally (no cloud required)

For this to work you'll need the backing services listed above to be up and available locally on your machine. The easiest way to do this is to use Docker, as instructed below...

1. Start the backing services.

````bash
$ cd <your-git-folder-for-this-repo>
$ docker-compose up -d
````

2. Build and run the tests.

````bash
$ ./gradlew test
````

> If our backing services are up, the tests should pass.

3. Run the code.

````bash
$ ./gradlew bootRun
````

> When working locally, JVM arguments are used to bootstrap the spring-connectors. You can see these in the `build.gradle` configuration file as as follows.

````groovy
def JvmSettings = [
        "-Dspring.cloud.appId=sc-connectors-demo",
        "-Dspring.profiles.active=cloud",
        "-Dspring.cloud.database=mysql://user:pass@localhost:3306/test_db",
        "-Dspring.cloud.rabbitmq=amqp://user:pass@localhost:5672/test_vhost",
        "-Dspring.cloud.mongodb=mongodb://localhost:27017/test_db"
]
````

4. Point your browser at http://localhost:8080. You should see a list showing the the status and details of the connected services. You'll see a similar list if you use actuator's `/health` endpoint.

````json
{
    id: "ee193ca4-2cb8-4ca7-b105-5d6b7c1ab415",
    mongo: "localhost:27017:UP",
    sql: "jdbc:mysql://localhost:3306/test_db?user=user&password=pass:UP",
    rabbit: "localhost:5672:UP"
}
````

In the code, you'll see that these resources have been created by the spring-cloud-connectors library in the `CloudConfig.java` class. You can also see them being `@Autowired` into the `Status.java` component...

````java
    @Autowired
    DataSource dataSource;
    
    @Autowired
    ConnectionFactory rabbitConnectionFactory;
    
    @Autowired
    MongoDbFactory mongoDbFactory;
````

## To Run in the Cloud (Pivotal Web Services)

I'm going to assume that you've used Cloud Foundry before. If you have not, follow the tutorials offered to you when you sign up for your Pivotal Web Services account.

1. Login to Pivotal Web Services from the cf CLI.

````bash
$ cd <your-git-folder-for-this-repo>
$ cf login -a api.run.pivotal.io
````

> If this is the first time you've used the cf CLI against PWS, you'll need your API key from your profile on the [Pivotal Network](http://network.pivotal.io).

2. Create the backing services.

````bash
$ cf create-service cleardb spark mysql
$ cf create-service cloudamqp lemur rabbit
$ cf create-service mlab sandbox mongo
````

> The application's `manifest.yml` file declares that these services (with these service names) should be available when you push the app to cloud foundry.

3. Build the code.

````bash
$ ./gradlew assemble
````

4. Push the demo app to Cloud Foundry.

> There is no need to bind the services to the app, it's automatic thanks to the `manifest.yml` as explained earlier.

````bash
$ cf push
````

> The push will *bind* the services to the app. The act of _binding_ puts the connection details for the service into the app's environment variables.

5. Point your browser at http://<your-app-url>.cfapps.io. You should see the same list showing the connected backing-services, but the URI's for the services will be different to the ones you saw last time.

## Run on PCF-Dev

You can also follow this exercise using PCF-Dev v0.20 rather than Pivotal Web Services. However, you'll have to use the `cf cups` feature to create your own `mongo` service in step 2 as PCF-Dev doesn't include a MongoDB service just yet.
 
````bash
$ cf create-service p-mysql 512mb mysql
$ cf create-service p-rabbitmq standard rabbit
$ cf cups mongo -p '{"uri":"mongodb://host.pcfdev.io:27017/test_db"}'
````

## How it works

Spring cloud connectors is trained to look for the backing service connection details in the _environment variables_ given to the app. When these details are detected, the connector library is configured to create a Spring `@Bean` in the `CloudConfig.java` class for the connection.

So for example, in the case of a MySQL database this would be a `javax.sql.DataSource` bean using the MySQL connection properties taken from the environment variables. For other resource types (RabbitMQ, MongoDB, Redis etc) an appropriate connection bean type is chosen.

## More information

For more information on the spring-cloud-connectors, take a look at the documentation...

- [Spring Cloud Connectors (General)](http://cloud.spring.io/spring-cloud-connectors/spring-cloud-connectors.html).
- [Spring Cloud Connectors - Cloud Foundry Connector](http://cloud.spring.io/spring-cloud-connectors/spring-cloud-cloud-foundry-connector.html).
- [Spring Cloud Connectors - Local Config Connector](http://cloud.spring.io/spring-cloud-connectors/spring-cloud-connectors.html#_local_configuration_connector).

There are already connectors for Heroku, and new cloud connectors can be created using the core.