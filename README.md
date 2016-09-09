# spring-cloud-connectors-demo

Demonstrates connecting to services hosted in Cloud Foundry from Spring Boot using spring-cloud-connectors. 

## Run locally (no cloud)

* Start the MySQL and RabbitMQ servers using Docker-compose:-

````bash
$ docker-compose up -d
````

* Run the demo:-

````bash
$ ./gradlew clean assemble bootRun
````

> Notes: When working locally, JVM environment argument properties are used (you can see these in the build.gradle file)

````groovy
bootRun {
	jvmArgs = [
            "-Dspring.cloud.appId=sc-connectors-demo",
            "-Dspring.profiles.active=cloud",
            "-Dspring.cloud.database=mysql://user:pass@localhost:3306/test_db",
            "-Dspring.cloud.rabbitmq=amqp://user:pass@localhost:5672/test_vhost",
    ]
}
````

## Run on the Cloud

* Login to PWS

````bash
$ cf login -a api.run.pivotal.io
````

* Provision the services you'll need

````bash
$ cf create-service cleardb spark mysql
$ cf create-service cloudamqp lemur rabbit
````

* Push the app (no need to bind services, it's automatic)

````bash
$ cf push
````

> Notes: When working on Pivotal Cloud Foundy, if you bind the RabbitMQ and MySQL services to the application, it will use the environment properties to make the necessary connections (even in PCF-Dev if you update to the java_buildpack 3.7 or higher). The application manifest declares that the MySQL and RabbitMQ services must be available when deploying.

````yml
applications:
- name: sc-connectors-demo
  path: build/libs/sc-connectors-demo.jar
  instances: 1
  memory: 1024M
  disk_quota: 1024M
  random-route: true
  services:
    - mysql
    - rabbit
````
