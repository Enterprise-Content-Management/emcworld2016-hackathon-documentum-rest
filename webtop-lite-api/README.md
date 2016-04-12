# Documentum REST Sample for Webtop Lite API

The following guide describes how to setup your Webtop Lite API server.

---
## 1. Project Dependencies

* Documentum Installation including the Documentum REST 7.2 and Content Server 7.2
* [Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) to compile the Java code
* [Maven 3.X](https://maven.apache.org/download.cgi) to build the project
* [Spring Tool Suite (STS) ](https://spring.io/tools/sts/all) which includes a built-in functionality to run the Spring Boot Application

## 2. Getting Started

Clone the repository to your local environment. 

##### Cloning the Repository

Use `git` to clone the repository into a new folder

    git clone git@github.com:Enterprise-Content-Management/emcworld2016-hackathon-documentum-rest.git/dctm-webtop-lite-api

##### Building the Application

Import the project into your STS workspace and load it as an existing maven project.

Edit the Spring Project's `application.properties` file which contains the configuration for Documentum Core REST server. 

Make sure that the link used to reference the ACS Server can be referenced from your machine, if you are using the Documentum Developer Image the host name will be demo-server you can edit as an entry in your hosts file pointing to the ACS Server.


## 3. Spring Boot Application

After configuring your link to the repositories, Run the `dctm-webtop-lite-api` Project as a Spring Boot App.



##### Testing the application

Use the following endpoints to get a list of cabinets available for the configured used

`http://localhost:8080/corerest/services/get/cabinets`





