package com.sample.boot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableAutoConfiguration
@EntityScan
@ComponentScan(basePackages="com.sample")
public class TomcatApplication {
	
	private static Log logger = LogFactory.getLog(TomcatApplication.class);

	public static void main(String[] args) throws Exception {
		
		logger.info("-- TomcatApplication --");
		SpringApplication.run(TomcatApplication.class, args);
	}
	
}
