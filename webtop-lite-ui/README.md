# Documentum REST Sample for Webtop Lite UI

The following guide describes how to setup your Webtop Lite Single Page Application (SPA). The project contains a File Manager and a PDF Viewer App used [Yeoman](http://yeoman.io) generator to generate App.

## 1. Project Dependencies

* [NodeJS](https://nodejs.org/) is used to run the app on a server.
* [AngularJS](https://angularjs.org/) is used as the frontend framework.
* [Gulp](http://gulpjs.com/) jobs for development, building, emulating, running your app, compiles and concatenates Sass files, local development server with live reload.

## 2. Getting Started

#### Backend Workflow

* Run Documentum VM and copy the IP address of your VM.
* Clone the Spring boot project `git clone git@github.com:Enterprise-Content-Management/emcworld2016-hackathon-documentum-rest.git/dctm-webtop-lite-api`. After cloning the project, update the properties file with your VM IP. Check the `README.md` file for detailed step by step instructions. 

#### Frontend Workflow

Clone the project using `git clone git@github.com:Enterprise-Content-Management/emcworld2016-hackathon-documentum-rest.git/dctm-webtop-lite-ui`. After the project is cloned navigate to the project directory.

If you don't have [bower](http://bower.io/) installed yet, run below CLI command to install it:
``` 
npm install -g bower
```
If you don't have [gulp](http://gulpjs.com/) installed yet, run below CLI command to install it:
```
npm install -g gulp
```
Run below CLI commands to build the project:
```
npm install --save-dev
bower install 
gulp
```
Run below CLI to start the SPA in a local web server (default port to 3000):
```
gulp serve
```
##### Note
Add the IP address of your machine that is running the Spring boot and port by changing the constant value `SPRING_API` in the `Config.js` file.


## 3. Troubleshoot
* if you get `Unable to connect github.com` while running `npm install` , `bower install` or cloning the project, this because you trying to connect through SSH, just change this connect type to HTTPS. Type this config command in the terminal `git config --global url."https://".insteadOf git://` and it problem should be fix
