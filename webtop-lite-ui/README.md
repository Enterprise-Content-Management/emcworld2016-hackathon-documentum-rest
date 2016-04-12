# Documentum REST Sample for Webtop Lite UI

The following guide describes how to setup your Webtop Lite Single Page Application (SPA). The project contains a File Manager and a PDF Viewer App used [Yeoman](http://yeoman.io) generator to generate App.

## 1. Project Dependencies

* [NodeJS](https://nodejs.org/) is used to run the app on a server.
* [AngularJS](https://angularjs.org/) is used as the frontend framework.
* [Gulp](http://gulpjs.com/) jobs for development, building, emulating, running your app, compiles and concatenates Sass files, local development server with live reload.

## 2. Getting Started

#### Backend Preparation

* Run Documentum VM and REST Services
* Clone the [Webtop Lite API](https://github.com/Enterprise-Content-Management/emcworld2016-hackathon-documentum-rest/tree/master/webtop-lite-api) project to your local workspace
>   git clone git@github.com:Enterprise-Content-Management/emcworld2016-hackathon-documentum-rest.git/webtop-lite-api
      
* Check the [README.md](https://github.com/Enterprise-Content-Management/emcworld2016-hackathon-documentum-rest/blob/master/webtop-lite-api/README.md) file for detailed instructions of running the Webtop Lite API Server
* Assumed that your Webtop Lite API Server is started up and has the root URL as `http://localhost:8080/webtop-lite-api-0.0.1-SNAPSHOT` 

#### Setup Project

* Clone the project using 
>  git clone git@github.com:Enterprise-Content-Management/emcworld2016-hackathon-documentum-rest.git/webtop-lite-ui

* Navigate to the project directory in your local copy
* Modify the constant value `SPRING_API` in the [Config.js](https://github.com/Enterprise-Content-Management/emcworld2016-hackathon-documentum-rest/blob/master/webtop-lite-ui/src/app/filemanager/providers/config.js) file. Change its value to the root URL of your Webtop Lite API Server, e.g. `http://localhost:8080/webtop-lite-api-0.0.1-SNAPSHOT`

#### Build and Run
* If you don't have [bower](http://bower.io/) installed yet, run below CLI command to install it:
``` 
npm install -g bower
```
* If you don't have [gulp](http://gulpjs.com/) installed yet, run below CLI command to install it:
```
npm install -g gulp
```
* Run below CLI commands to build the project:
```
npm install --save-dev
bower install 
gulp
```
* Run below CLI to start the SPA in a local web server (default port to 3000):
```
gulp serve
```
Your web browser will be promoted to access the URL `http://localhost:3000`. If it did not get promoted, try to manually access this URL in your web browser.


## 3. Troubleshoot
* if you get `Unable to connect github.com` while running `npm install` , `bower install` or cloning the project, this because you trying to connect through SSH, just change this connect type to HTTPS. Type this config command in the terminal `git config --global url."https://".insteadOf git://` and it problem should be fix
