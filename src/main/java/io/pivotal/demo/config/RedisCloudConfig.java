package io.pivotal.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@Profile("cloud")
public class RedisCloudConfig extends AbstractCloudConfig {

	private static final Logger logger = LoggerFactory.getLogger(RedisCloudConfig.class);
	
	static {
		logger.info("Loading RedisCloudConfig");
	}
	@Bean
	public RedisConnectionFactory redisConnection()
	{
		logger.info("Using a cloud configuration");
		return connectionFactory().redisConnectionFactory();
	}
}
