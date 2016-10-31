package io.pivotal.demo.web;

import javax.validation.Valid;

import io.pivotal.demo.domain.Person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/persons")
public class PersonController {
	private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
	static {
		logger.info("Loading PersonController");
	}
	private CrudRepository<Person, String> repository;
	
	@Autowired
	public PersonController(CrudRepository<Person, String> repository)
	{
		logger.info("Autowired... Setting the repository to "+repository);
		this.repository = repository;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Person> persons()
	{
		return repository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.PUT)
    public Person add(@RequestBody @Valid Person person) {
        logger.info("Adding person " + person.getEmailAddress());
        return repository.save(person);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Person update(@RequestBody @Valid Person person) {
        logger.info("Updating person " + person.getEmailAddress());
        return repository.save(person);
    }

    @RequestMapping(value = "/{emailAddress:.+}", method = RequestMethod.GET)
    public Person getById(@PathVariable String emailAddress) {
        logger.info("Getting person " + emailAddress);
        return repository.findOne(emailAddress);
    }

    @RequestMapping(value = "/{emailAddress:.+}", method = RequestMethod.DELETE)
    public void deleteById(@PathVariable String emailAddress) {
        logger.info("Deleting person " + emailAddress);
        repository.delete(emailAddress);
    }	
}
