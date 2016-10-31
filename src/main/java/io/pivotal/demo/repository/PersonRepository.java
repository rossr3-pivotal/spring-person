package io.pivotal.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.pivotal.demo.domain.Person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;

public class PersonRepository implements CrudRepository<Person, String> {
	private static final Logger logger = LoggerFactory.getLogger(PersonRepository.class);
	static {
		logger.info("Loading PersonRepository");
	}
	
	public static final String PERSONS_KEY = "persons";
	
	private final HashOperations<String, String, Person> hashOps;
	
	public PersonRepository(RedisTemplate<String, Person> redisTemplate) 
	{
		logger.info("Creating a person repository");
		this.hashOps = redisTemplate.opsForHash();
	}

	@Override
	public long count() {
		return hashOps.keys(PERSONS_KEY).size();
	}

	@Override
	public void delete(String emailAddress) {
		logger.info("Repository: deleting by emailAddress: " + emailAddress);
		hashOps.delete(PERSONS_KEY, emailAddress);
	}

	@Override
	public void delete(Person person) {
		logger.info("Repository: deleting person " + person);
		hashOps.delete(PERSONS_KEY,  person.getEmailAddress());
	}

	@Override
	public void delete(Iterable<? extends Person> persons) {
		for (Person p : persons)
		{
			delete(p);
		}		
	}

	@Override
	public void deleteAll() {
		Set<String> emails = hashOps.keys(PERSONS_KEY);
		for (String email : emails)
		{
			delete(email);
		}
		
	}

	@Override
	public boolean exists(String emailAddress) {
		logger.info("Does email address exist?" + emailAddress);
		return hashOps.hasKey(PERSONS_KEY,  emailAddress);
	}

	@Override
	public Iterable<Person> findAll() {
		logger.info("Finding all persons");
		return hashOps.values(PERSONS_KEY);
	}

	@Override
	public Iterable<Person> findAll(Iterable<String> emailAddresses) {
		logger.info("Finding all persons: " + emailAddresses);
		return hashOps.multiGet(PERSONS_KEY, convertIterableToList(emailAddresses));
	}

	@Override
	public Person findOne(String emailAddress) {
		logger.info("Finding One by email address: " + emailAddress);
		return hashOps.get(PERSONS_KEY, emailAddress);
	}

	@Override
	public <S extends Person> S save(S person) {
		hashOps.put(PERSONS_KEY, person.getEmailAddress(), person);
		
		return person;
	}

	@Override
	public <S extends Person> Iterable<S> save(Iterable<S> persons) {
		List<S> result = new ArrayList();
		
		for(S entity : persons)
		{
			save(entity);
			result.add(entity);
		}

		return result;
	}
	
	private <T> List<T> convertIterableToList(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        for (T object : iterable) {
            list.add(object);
        }
        return list;
    }

}
