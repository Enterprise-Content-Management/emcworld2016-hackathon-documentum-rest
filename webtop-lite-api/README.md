# Documentum REST Sample for Webtop Lite API

The following guide describes how to setup your Webtop Lite API Server.

---
## 1. Project Dependencies

* [Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) to compile the Java code
* [Maven 3.X](https://maven.apache.org/download.cgi) to build the project
* [Spring Tool Suite (STS) ](https://spring.io/tools/sts/all) which includes a built-in functionality to run the Spring Boot Application

## 2. Getting Started

#### Backend Preparation

* Documentum Installation including Content Server 7.2 and the Documentum REST 7.2
* Assumed that Documentum REST Services is started up and has the root URL as `http://localhost:8080/dctm-rest` 

Clone the repository to your local environment. 

#### Setup Project

* Clone the project using 
>  git clone git@github.com:Enterprise-Content-Management/emcworld2016-hackathon-documentum-rest.git/webtop-lite-api

* Modify the constant values in the [application.properties](https://github.com/Enterprise-Content-Management/emcworld2016-hackathon-documentum-rest/blob/master/webtop-lite-api/src/main/resources/application.properties) file. Change their values to accommodate your Documentum deployment.
>  ## assummed that your Documentum REST Server is: http://localhost:8080/dctm-rest
spring.thymeleaf.cache:false
documentum.corerest.host:localhost
documentum.corerest.username:dmadmin
documentum.corerest.password:password
documentum.corerest.repo:REPO
server.port: 8080
management.port: 9001
management.address: localhost
translation.enabled:true

##### Building the Application from STS

* Import the project into your STS workspace and load it as an existing maven project. 
* After configuring your link to the repositories, Run the `dctm-webtop-lite-api` Project as a Spring Boot App.

##### Building the Application from CLI

* If you have standalone Maven installed, you can build the project from CLI and get the WAR file to deploy.
```
mvn clean install -DskipTests
```

#### Tips
Make sure that the link used to reference the ACS Server can be referenced from your machine, if you are using the Documentum Developer Image the host name will be demo-server you can edit as an entry in your hosts file pointing to the ACS Server.


##### Testing the application

Use the following endpoints to get a list of cabinets available for the configured used

`http://localhost:8080/webtop-lite-api-0.0.1-SNAPSHOT/services/get/cabinets`





