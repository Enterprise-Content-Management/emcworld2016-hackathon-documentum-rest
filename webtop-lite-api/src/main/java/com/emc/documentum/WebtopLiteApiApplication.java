package com.emc.documentum;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableAutoConfiguration
public class WebtopLiteApiApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(WebtopLiteApiApplication.class, args);
		System.out.println(ctx.getApplicationName());
	}
}
