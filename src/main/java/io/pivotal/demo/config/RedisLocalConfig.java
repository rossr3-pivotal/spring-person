package io.pivotal.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
@Profile("default")
public class RedisLocalConfig {
	private static final Logger logger = LoggerFactory.getLogger(RedisLocalConfig.class);
	
	static {
		logger.info("Loading RedisLocalConfig");
	}
	
	@Bean 
	public RedisConnectionFactory redisConnection()
	{
		logger.info("Local profile is selected");
		return new JedisConnectionFactory();
	}
}
