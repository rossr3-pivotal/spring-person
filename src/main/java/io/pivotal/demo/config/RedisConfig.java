package io.pivotal.demo.config;

import io.pivotal.demo.domain.Person;
import io.pivotal.demo.repository.PersonRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
	private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
	
	static {
		logger.info("Loading RedisConfig");
	}
	
	@Bean
	public PersonRepository repository(RedisTemplate<String, Person> redisTemplate)
	{
		logger.info("creating person repository");
		return new PersonRepository(redisTemplate);
	}
	
	@Bean
	public RedisTemplate<String, Person> redisTemplate(RedisConnectionFactory redisConnectionFactory)
	{
		logger.info("crating redisTemplate");
		RedisTemplate<String, Person> template = new RedisTemplate();
		
		template.setConnectionFactory(redisConnectionFactory);
		
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		RedisSerializer<Person> personSerializer = new Jackson2JsonRedisSerializer<>(Person.class);
		
		template.setKeySerializer(stringSerializer);
		template.setValueSerializer(personSerializer);
		template.setHashKeySerializer(stringSerializer);
		template.setHashValueSerializer(personSerializer);
		
		return template;
	}
}
