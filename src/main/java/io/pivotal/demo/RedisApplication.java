package io.pivotal.demo;

import io.pivotal.demo.config.SpringApplicationContextInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.cloudfoundry.CloudFoundryConnector;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class RedisApplication {

	private static final Logger logger = LoggerFactory.getLogger(RedisApplication.class);
	
	public static void main(String[] args) 
	{
		logger.info("Starting RedisApplicaiton");;
		CloudFoundryConnector cfConnector = new CloudFoundryConnector();
		if (cfConnector.isInMatchingCloud()) 
		{
			logger.info("Running in the cloud");
		}
		else
		{
			logger.info("Not running in the cloud");
		}
		
		logger.info("starting app...");
		
        new SpringApplicationBuilder(RedisApplication.class).
             initializers(new SpringApplicationContextInitializer())
             .application()
             .run(args);
        
        logger.info("finished app...");
	}
}
