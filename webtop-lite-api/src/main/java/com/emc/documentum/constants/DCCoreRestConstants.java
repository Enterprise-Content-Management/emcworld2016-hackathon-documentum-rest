package com.emc.documentum.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class DCCoreRestConstants {

	@Value("${documentum.corerest.host}")
	public  String host;

	@Value("${documentum.corerest.repo}")
	public  String repo;

	@Value("http://${documentum.corerest.host}:8080/dctm-rest/repositories/${documentum.corerest.repo}/currentuser")
	public  String currentUserURI;
	@Value("http://${documentum.corerest.host}:8080/dctm-rest/repositories/${documentum.corerest.repo}?dql=")
	public  String dqlQuery;
	@Value("http://${documentum.corerest.host}:8080/dctm-rest/repositories/${documentum.corerest.repo}/objects")
	public  String fetchObjectUri;
	@Value("${documentum.corerest.username}")
	public  String username;
	@Value("${documentum.corerest.password}")
	public  String password;
	@Value("http://${documentum.corerest.host}:8080/dctm-rest/repositories/${documentum.corerest.repo}/cabinets")
	public  String fetchCabinetURI;
	@Value("http://${documentum.corerest.host}:8080/dctm-rest/repositories/${documentum.corerest.repo}/folders")
	public  String fetchFolderURI;

}