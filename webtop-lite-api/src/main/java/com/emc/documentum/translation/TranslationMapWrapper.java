package com.emc.documentum.translation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

@Component
public class TranslationMapWrapper {

	HashMap<String, Resource> resourcesMap = new HashMap<String, Resource>();
	HashMap<String, Properties> propertiesMap = new HashMap<String, Properties>();
	HashMap<String, BidiMap<String,String>> bidiMapsMap = new HashMap<String, BidiMap<String,String>>();

	public TranslationMapWrapper() {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		String packageSearchPath = "classpath*:*.mapping.properties";

		try {
			Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
			String fileName = null ;
			for (Resource resource : resources) {
				fileName = resource.getFilename() ;
				resourcesMap.put(fileName, resource);
				propertiesMap.put(fileName , getPropertiesFromResource(fileName)) ;
				bidiMapsMap.put(fileName , getBiDiMapFromResource(fileName)) ;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public BidiMap<String,String> getBiDiMapFromResource(String resourceName) {
		try {
			Resource resource = resourcesMap.get(resourceName);
			Properties prop = new Properties();
			if (resource.isReadable()) {
				InputStream inputStream = resource.getInputStream();
				if (inputStream != null) {
					prop.load(inputStream);
					BidiMap<String,String> map1 = fillMapWithProperties(prop) ;
					return map1 ;
				} else {
					throw new FileNotFoundException("property file '" + resource.getFilename() + "' not found in the classpath");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null ;
	}
	
	
	public BidiMap<String,String> fillMapWithProperties(Properties properties)
	{
		BidiMap<String,String> map1 = new DualHashBidiMap<String,String>();
		for (String name: properties.stringPropertyNames())
		{
		    map1.put(name, properties.getProperty(name));
		}
		return map1 ;
	}
	
	public Properties getPropertiesFromResource(String resourceName) {
		try {
			Resource resource = resourcesMap.get(resourceName);
			Properties prop = new Properties();
			if (resource.isReadable()) {
				InputStream inputStream = resource.getInputStream();
				if (inputStream != null) {
					prop.load(inputStream);
					return prop ;
				} else {
					throw new FileNotFoundException("property file '" + resource.getFilename() + "' not found in the classpath");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null ;
	}
}