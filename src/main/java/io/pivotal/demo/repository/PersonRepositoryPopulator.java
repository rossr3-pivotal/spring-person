package io.pivotal.demo.repository;

import io.pivotal.demo.domain.Person;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.init.Jackson2ResourceReader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PersonRepositoryPopulator implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {
	
	private static final Logger logger = LoggerFactory.getLogger(PersonRepositoryPopulator.class);
	
	static {
		logger.info("Loading PersonRepositoryPopulator");
	}
	
    private final Jackson2ResourceReader resourceReader;
    private final Resource sourceData;
    private ApplicationContext applicationContext;
    
    public PersonRepositoryPopulator()
    {
    	logger.info("PersonRepositoryPopulator constructor");
    	ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        resourceReader = new Jackson2ResourceReader(mapper);
        logger.info("attempting to load the persons.json file");
        sourceData = new ClassPathResource("persons.json");
        logger.info("The source data is " + sourceData);
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		 logger.info("setting the application context to " + applicationContext);
		 this.applicationContext = applicationContext;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("application event " + event);
		logger.info("applicationContext is " + applicationContext);
		 if (event.getApplicationContext().equals(applicationContext)) {
			    logger.info("app context matches");
	            CrudRepository personRepository =
	                    BeanFactoryUtils.beanOfTypeIncludingAncestors(applicationContext, CrudRepository.class);
	            logger.info("personRepository is " + personRepository);
	            if (personRepository != null && personRepository.count() == 0) {
	            	logger.info("populating the repository with default data");
	                populate(personRepository);
	            }
	            else
	            {
	            	logger.info("repostitory already has records in it");
	            }
	        }
	}
	
    @SuppressWarnings("unchecked")
    public void populate(CrudRepository repository) {
    	logger.info("attempting to populate the repository");
        Object entity = getEntityFromResource(sourceData);
        logger.info("The entity is "+ entity);

        if (entity instanceof Collection) {
        	logger.info("We have a collection of entities...");
            for (Person person : (Collection<Person>) entity) {
                if (person != null) {
                    repository.save(person);
                }
            }
        } else {
        	logger.info("only a single entity");
            repository.save(entity);
        }
    }

    private Object getEntityFromResource(Resource resource) {
        try {
            return resourceReader.readFrom(resource, this.getClass().getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }	
	

}
