package io.pivotal.demo.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
	private static final Log logger = LogFactory.getLog(SpringApplicationContextInitializer.class);
	
	static 
	{
		logger.info("Loading SpringApplicationContextInitializer");
	}

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		logger.info("Initialing application context...");
		
		Cloud cloud = getCloud();
		
		if (cloud == null)
			return;
		
	}
	
    private Cloud getCloud() {
        try {
            CloudFactory cloudFactory = new CloudFactory();
            return cloudFactory.getCloud();
        } catch (CloudException ce) {
            return null;
        }
    }
}
