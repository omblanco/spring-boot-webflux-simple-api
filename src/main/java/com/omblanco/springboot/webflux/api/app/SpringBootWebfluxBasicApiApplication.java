package com.omblanco.springboot.webflux.api.app;

import java.util.Date;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.omblanco.springboot.webflux.api.app.model.entity.User;
import com.omblanco.springboot.webflux.api.app.services.UserService;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * Clase principal de configuración de Spring Boot
 * @author oscar.martinezblanco
 *
 */
@SpringBootApplication
public class SpringBootWebfluxBasicApiApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(SpringBootWebfluxBasicApiApplication.class);
    
    @Value("${spring.datasource.maximum-pool-size}")
    private int connectionPoolSize;
    
    @Autowired
    private UserService userService;
    
    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebfluxBasicApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User john = new User(null, "John", "Doe", "john@mail.com", new Date());
        User oscar = new User(null, "Oscar", "Suarez", "oscar@mail.com", new Date());
        User maria = new User(null, "Maria", "Salgado", "salgado@mail.com", new Date());
        User manuel = new User(null, "Manuel", "Lopez", "manuel@mail.com", new Date());
        
        
        Flux.just(john, oscar, maria, manuel)
        .flatMap(userService::save)
        .subscribe(user -> LOG.info("Usuario insertado: ".concat(user.toString())));
    }
    
    @Bean
    public Scheduler jdbcScheduler() {
        return Schedulers.fromExecutor(Executors.newFixedThreadPool(connectionPoolSize));
    }
}
