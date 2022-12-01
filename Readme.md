# App to convert a json file to a flat structure


## Requirements

For building and running the application you need:

- [JDK 11](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.petrhalik.converter.ConverterApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn clean spring-boot:run
```
or use a Maven wrapper in root application directory

```shell
$ ./mvnw clean spring-boot:run
```

### Swagger console:
http://localhost:8080/swagger-ui.htm

Application is running on port 8080

### Request URL for localhost
POST: http://localhost:8080/converter/json?outputFormat=csv

body:  json to be converted

Input format: json

Output formal: cvs
