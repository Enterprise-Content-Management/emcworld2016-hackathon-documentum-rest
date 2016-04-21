# Documentum REST Sample for AngularJS File Manager UI

The following guide describes how to setup your Documentum AngularJS File Manager Single Page Application (SPA). The project contains basic folder/document management and a PDF viewer.

## 1. Project dependencies

* [NodeJS](https://nodejs.org/) is used to run the app on a server.
* [AngularJS](https://angularjs.org/) is used as the frontend framework.
* [Gulp](http://gulpjs.com/) jobs for development, building, emulating, running your app, compiles and concatenates Sass files, local development server with live reload.

##### Tip
>  You don't need to setup anything at this moment, please keep patience to follow next sections to install software.

## 2. Getting started

#### Backend preparation

* Assumed that you have Documentum Content Server 7.2 and Docmentum REST Services 7.2 up and running
* Assumed that your Documentum AngularJS File Manager API server up and running with the root URL `http://localhost:7000`

##### Tip
>  Check the [README.md](https://github.com/Enterprise-Content-Management/emcworld2016-hackathon-documentum-rest/blob/master/file-manager-api/README.md) file for detailed instructions of running the Documentum AngularJS File Manager API server.

#### Setting up

* Clone the project using `git clone git@github.com:Enterprise-Content-Management/emcworld2016-hackathon-documentum-rest.git/file-manager-ui`
* Navigate to the project directory in your local copy
* Modify the constant value `SPRING_API` in the [Config.js](https://github.com/Enterprise-Content-Management/emcworld2016-hackathon-documentum-rest/blob/master/file-manager-ui/src/app/filemanager/providers/config.js) file. Change its value to the root URL of your Documentum AngularJS File Manager API Server, e.g. `http://localhost:7000`

#### Build and Run
* If you don't have [NodeJS](https://nodejs.org/) installed yet, download and install it.
>   NodeJS 4.x is recommended. You might run into build errors using NodeJS 5.x.
* If you don't have [Git](https://git-scm.com/) installed yet, download and install it.
* If you don't have [bower](http://bower.io/) installed yet, run below CLI command to install it:
``` 
> npm install -g bower
```
* If you don't have [gulp](http://gulpjs.com/) installed yet, run below CLI command to install it:
```
> npm install -g gulp
```
* Run below CLI commands in sequence to build the project:
```
> npm install --save-dev
> bower install
> gulp
```
* Run below CLI to start the SPA in a local web server (default port to 3000):
```
> gulp serve
```
Your web browser will be promoted to access the URL `http://localhost:3000`. If it did not get promoted, try to manually access this URL in your web browser.
> Goole Chrome and Mozilla Firefox are recommended. Microsoft Internet Explorer does not has the full support for HTML5 features demonstrated in this app.


## 3. Troubleshoot
* if you get `Unable to connect github.com` while running `npm install` , `bower install` or cloning the project, this because you trying to connect through SSH, just change this connect type to HTTPS. Type this config command in the terminal `git config --global url."https://".insteadOf git://` and it problem should be fix
