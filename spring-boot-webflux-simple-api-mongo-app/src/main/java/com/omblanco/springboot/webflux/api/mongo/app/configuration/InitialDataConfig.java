package com.omblanco.springboot.webflux.api.mongo.app.configuration;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.omblanco.springboot.webflux.api.mongo.app.model.entity.User;
import com.omblanco.springboot.webflux.api.mongo.app.model.repositories.UserRepository;

import reactor.core.publisher.Flux;

/**
 * Carga los datos iniciales de usuarios para el profile de 
 * pruebas
 * see https://docs.spring.io/spring-data/mongodb/docs/2.0.9.RELEASE/reference/html/#core.repository-populators
 * @author oscar.martinezblanco
 *
 */
@Profile("!stage & !pro")
@Configuration
public class InitialDataConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitialDataConfig.class);
    
//    @Autowired
//    private UserRepository userRepository;
    
//    @Value("classpath:data.json")
//    private Resource initialData;

    /**
     * https://jira.spring.io/browse/DATACMNS-1133
     * Jackson2RepositoryPopulatorFactoryBean no es compatible con Jackson2RepositoryPopulatorFactoryBean
     * @param objectMapper
     * @return
     */
//    @Bean
//    @Autowired
//    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator(ObjectMapper objectMapper){
//        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
//        factory.setMapper(objectMapper);
//        factory.setResources(Arrays.asList(initialData).toArray(new Resource[] {}));
//        return factory;
//    }
    
    @Bean
    public ApplicationRunner loadInitalData(UserRepository userRepository) {
        return applicationRunner -> {
//          Limpiamos si hay usuarios
            userRepository.deleteAll();
            
            User user1 = new User(null, "John", "Doe", "john@mail.com", new Date(), "$2a$10$vUE9JNc3ZflWL6u4HFH2kOEHWgNIahyAxoUoaZ1g0rsHJ3y9kzhwy");
            User user2 = new User(null, "Oscar", "Suarez", "oscar@mail.com", new Date(), "$2a$10$vUE9JNc3ZflWL6u4HFH2kOEHWgNIahyAxoUoaZ1g0rsHJ3y9kzhwy");
            User user3 = new User(null, "Maria", "Salgado", "salgado@mail.com", new Date(), "$2a$10$vUE9JNc3ZflWL6u4HFH2kOEHWgNIahyAxoUoaZ1g0rsHJ3y9kzhwy");
            User user4 = new User(null, "Manuel", "Lopez", "manuel@mail.com", new Date(), "$2a$10$vUE9JNc3ZflWL6u4HFH2kOEHWgNIahyAxoUoaZ1g0rsHJ3y9kzhwy");
            
            Flux.just(user1, user2, user3, user4)
                .flatMap(userRepository::save)
                .subscribe(user -> LOGGER.info("Usuario insertado: " + user));

        };
    }
}
