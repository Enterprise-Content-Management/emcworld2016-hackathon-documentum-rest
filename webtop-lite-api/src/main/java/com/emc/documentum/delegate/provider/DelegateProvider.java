package com.emc.documentum.delegate.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.emc.documentum.delegates.DocumentumDelegate;
import com.emc.documentum.exceptions.DelegateNotFoundException;

@Scope(value="application")
@Component
public class DelegateProvider {
	
	@Autowired
	private ApplicationContext context;
	
	public static String PREFIX = "Documentum" ;
	public static String SUFFIX = "Delegate" ;
	
	
	public DocumentumDelegate getDelegate(String delegateKey) throws DelegateNotFoundException
	{
		if(context.getBean(PREFIX+delegateKey+SUFFIX) != null)
		{
			return (DocumentumDelegate) context.getBean(PREFIX+delegateKey+SUFFIX);
		}
		else
		{
			throw new DelegateNotFoundException() ;
		}
	}
}
