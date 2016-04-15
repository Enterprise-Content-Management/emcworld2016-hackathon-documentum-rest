package com.emc.documentum.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CommonLogger {
	private static final Log LOGGER = LogFactory.getLog(CommonLogger.class);

	@AfterReturning("execution(* com.emc.documentum.filemanager.controller.*.*(..))")
	public void afterExecution(JoinPoint point) {
		LOGGER.debug("finished execution of " + point.getSignature().toShortString());
	}
	
	@Before("execution(* com.emc.documentum.filemanager.controller.*.*(..))")
	public void beforeExecution(JoinPoint point) {
		LOGGER.debug("starting execution of " + point.getSignature().toShortString());
	}

	@AfterThrowing(pointcut="execution(* com.emc.documentum.filemanager.controller.*.*(..))",throwing="ex")
	public void afterException(JoinPoint point,Throwable ex) {
		LOGGER.error("execution of " + point.getSignature().toShortString() + " failed.");
		LOGGER.error(ex.getMessage(), ex);
	}
}
