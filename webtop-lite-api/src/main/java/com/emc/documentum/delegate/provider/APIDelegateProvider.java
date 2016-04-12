package com.emc.documentum.delegate.provider;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.emc.documentum.delegates.DocumentumDelegate;
import com.emc.documentum.exceptions.DelegateNotFoundException;

@Scope(value = "application")
@Component
public class APIDelegateProvider {

	HashMap<String, DocumentumDelegate> apiDelegates;

	@Autowired
	private ApplicationContext context;

	@PostConstruct
	protected void initialize() {
		apiDelegates = new HashMap<>();
		Map<String, DocumentumDelegate> delegates = context.getBeansOfType(DocumentumDelegate.class);
		for (DocumentumDelegate delegate : delegates.values()) {
			apiDelegates.put(delegate.getIdentifier(), delegate);
		}
	}

	public DocumentumDelegate getDelegate(String delegateIdentifier) throws DelegateNotFoundException {
		if (apiDelegates.containsKey(delegateIdentifier)) {
			return apiDelegates.get(delegateIdentifier);
		}
		throw new DelegateNotFoundException(delegateIdentifier);
	}
}
