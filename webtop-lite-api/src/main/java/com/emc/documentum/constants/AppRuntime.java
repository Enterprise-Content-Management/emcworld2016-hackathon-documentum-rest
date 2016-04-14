package com.emc.documentum.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class AppRuntime {

	@Value("${documentum.corerest.host}")
	public  String host;

	@Value("${documentum.corerest.repo}")
	public  String repo;

	@Value("${documentum.corerest.username}")
	public  String username;

	@Value("${documentum.corerest.password}")
	public  String password;

	@Value("http://${documentum.corerest.host}:${documentum.corerest.port}/dctm-rest")
	public  String contextRootUri;

}