# Documentum REST Sample for Webtop Lite API

The following guide describes how to setup your Webtop Lite API Server.

---
## 1. Project dependencies

* [JDK 7u80](http://www.oracle.com/technetwork/cn/java/javase/downloads/jdk7-downloads-1880260.html) or [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) to compile the Java code
>   JDK 7u79 and earlier versions do not support Spring Cross-Origin annotation.
* [Maven 3.x](https://maven.apache.org/download.cgi) to build the project
>   Make sure Maven bin is added to the PATH, e.g., you can run `mvn -v` in CLI.
* Optionally, [Spring Tool Suite (STS) ](https://spring.io/tools/sts/all) which includes a built-in functionality to run the Spring Boot Application
>   If you have no love with STS, you are free to use any other Java IDEs that support Maven

## 2. Getting started

#### Backend preparation

* Documentum installation including Content Server 7.2 and the Documentum REST 7.2 (with the latest patch)
* Assumed that Documentum REST Services is up and running with the root URL as `http://localhost:8080/dctm-rest`

#### Setting up

* Clone the project using `git clone git@github.com:Enterprise-Content-Management/emcworld2016-hackathon-documentum-rest.git/webtop-lite-api`
* Navigate to the project directory in your local copy
* Modify the constant values in the [application.properties](https://github.com/Enterprise-Content-Management/emcworld2016-hackathon-documentum-rest/blob/master/webtop-lite-api/src/main/resources/application.properties) file. Change their values to accommodate your Documentum REST Services deployment.
```
## assummed that your Documentum REST Server is: http://localhost:8080/dctm-rest
## customize below properties
documentum.corerest.host:localhost
documentum.corerest.port:8080
documentum.corerest.username:dmadmin
documentum.corerest.password:password
documentum.corerest.repo:REPO
```
##### Tip
>   Before you build this project, Documentum REST Services must be up and running with the right port specified in `application.properties`.
You can simply check its availability with the Home Document resource, e.g. `http://localhost:8080/dctm-rest/services`.

#### Building from STS

* Import the project into your STS workspace and load it as an existing maven project. 
* After configuring your link to the repositories, run the `dctm-webtop-lite-api` Project as a Spring Boot App.
* Alternatively, you can also run the class `WebtopLiteApiApplication` as a Java program.
* By default, the root URL is `http://localhost:7000/` by default.

#### Building from CLI

* If you have the standalone Maven installed, you can build the project from CLI and get the WAR file to deploy.
```
> mvn clean spring-boot:run
```
* The root URL is `http://localhost:7000/` by default.


#### Testing

Use the following endpoints to get the first 5 cabinets available for the configured repository using below URI:

`http://localhost:7000/api/test`





