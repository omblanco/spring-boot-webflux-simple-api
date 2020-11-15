package com.omblanco.springboot.webflux.api.app;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.omblanco.springboot.webflux.api.app.services.UserService;
import com.omblanco.springboot.webflux.api.app.web.dto.UserDTO;

import reactor.core.publisher.Flux;

/**
 * Clase principal de configuración de Spring Boot
 * @author oscar.martinezblanco
 *
 */
@SpringBootApplication
public class SpringBootWebfluxBasicApiApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(SpringBootWebfluxBasicApiApplication.class);
    
    @Autowired
    private UserService userService;
    
    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebfluxBasicApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        UserDTO john = new UserDTO(null, "John", "Doe", "john@mail.com", new Date());
        UserDTO oscar = new UserDTO(null, "Oscar", "Suarez", "oscar@mail.com", new Date());
        UserDTO maria = new UserDTO(null, "Maria", "Salgado", "salgado@mail.com", new Date());
        UserDTO manuel = new UserDTO(null, "Manuel", "Lopez", "manuel@mail.com", new Date());
        
        
        Flux.just(john, oscar, maria, manuel)
        .flatMap(userService::save)
        .subscribe(user -> LOG.info("Usuario insertado: ".concat(user.toString())));
    }
}
