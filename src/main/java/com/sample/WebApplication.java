package com.sample;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableAutoConfiguration
@EntityScan
@ComponentScan(basePackages="com.sample")
public class WebApplication {
	
	private static Log logger = LogFactory.getLog(WebApplication.class);

	public static void main(String[] args) throws Exception {
		
		logger.info("-- WebApplication --");
		SpringApplication.run(WebApplication.class, args);
	}
	
}
