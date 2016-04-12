package com.emc.documentum.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CommonLogger {
	private Logger log = Logger.getLogger(getClass().getCanonicalName());

	@AfterReturning("execution(* com.emc.documentum.services.rest.*.*(..))")
	public void afterExecution(JoinPoint point) {

		log.info("finished execution of "+point.getSignature().toShortString());
	}
	
	@Before("execution(* com.emc.documentum.services.rest.*.*(..))")
	public void beforeExecution(JoinPoint point) {
		log.info("starting execution of "+point.getSignature().toShortString());
	}

	@AfterThrowing(pointcut="execution(* com.emc.documentum.services.rest.*.*(..))",throwing="ex")
	public void afterException(JoinPoint point,Throwable ex) {
		log.severe("execution of " + point.getSignature().toShortString()+" failed.");
		log.log(Level.SEVERE, ex.getMessage(), ex);
	}
}
