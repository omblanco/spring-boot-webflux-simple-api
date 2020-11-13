package com.omblanco.springboot.webflux.api.app.web.controllers;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.omblanco.springboot.webflux.api.app.model.entity.User;
import com.omblanco.springboot.webflux.api.app.services.UserService;
import com.omblanco.springboot.webflux.api.app.utils.BaseApiConstants;

import reactor.core.publisher.Mono;

/**
 * Test de integración para el UserController
 * @author oscar.martinezblanco
 *
 */
@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureWebTestClient
public class UserControllerTests {

    @Autowired
    private WebTestClient client;
    
    @Autowired
    private UserService userService;
    
    @Test
    public void findAllTest() {
        client.get()
        .uri(BaseApiConstants.USER_BASE_URL_V1)
        .accept(MediaType.APPLICATION_JSON)
        .exchange().expectStatus()
        .isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(User.class)
        .consumeWith(response -> {
            List<User> users = response.getResponseBody();
            users.forEach(user -> {
                System.out.println(user);
            });
            
            Assertions.assertThat(users.size() > 0).isTrue();
        });
    }
    
    @Test
    public void getByidTest() {
        User user = userService.findAll().blockFirst();
        
        client.get()
        .uri(BaseApiConstants.USER_BASE_URL_V1.concat("/{id}"), Collections.singletonMap("id", user.getId()))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(User.class)
        .consumeWith(response -> {
            User userResponse = response.getResponseBody();
            Assertions.assertThat(userResponse.getId()).isNotNull();
            Assertions.assertThat(userResponse.getId() > 0).isTrue();
            Assertions.assertThat(userResponse.getId()).isEqualTo(user.getId());
            Assertions.assertThat(userResponse.getBirthdate()).isEqualTo(user.getBirthdate());
            Assertions.assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
            Assertions.assertThat(userResponse.getName()).isEqualTo(user.getName());
            Assertions.assertThat(userResponse.getSurname()).isEqualTo(user.getSurname());
        });
    }
    
    @Test
    public void postTest() {
        User user = new User(null, "Fulano", "De tal", "fulano@mail.com", new Date());
        
        client.post()
        .uri(BaseApiConstants.USER_BASE_URL_V1)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(user), User.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(User.class).consumeWith(response -> {
            User userResponse = response.getResponseBody();
            Assertions.assertThat(userResponse.getId()).isNotNull();
            Assertions.assertThat(userResponse.getId() > 0).isTrue();
            Assertions.assertThat(userResponse.getBirthdate()).isEqualTo(user.getBirthdate());
            Assertions.assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
            Assertions.assertThat(userResponse.getName()).isEqualTo(user.getName());
            Assertions.assertThat(userResponse.getSurname()).isEqualTo(user.getSurname());
        });
    }
    
    @Test
    public void putTest() {
        User user = userService.findAll().blockFirst();
        
        user.setEmail("email@mail.com");
        user.setName("Name");
        user.setSurname("Surname");
        
        client.put()
        .uri(BaseApiConstants.USER_BASE_URL_V1.concat("/{id}"), Collections.singletonMap("id", user.getId()))
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(user), User.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(User.class)
        .consumeWith(response -> {
            User userResponse = response.getResponseBody();
            Assertions.assertThat(userResponse.getId()).isNotNull();
            Assertions.assertThat(userResponse.getId() > 0).isTrue();
            Assertions.assertThat(userResponse.getId()).isEqualTo(user.getId());
            Assertions.assertThat(userResponse.getBirthdate()).isEqualTo(user.getBirthdate());
            Assertions.assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
            Assertions.assertThat(userResponse.getName()).isEqualTo(user.getName());
            Assertions.assertThat(userResponse.getSurname()).isEqualTo(user.getSurname());
        });
    }
    
    @Test
    public void deleteTest() {
        User user = userService.findAll().blockFirst();
        client.delete()
            .uri(BaseApiConstants.USER_BASE_URL_V1.concat("/{id}"), Collections.singletonMap("id", user.getId()))
            .exchange()
            .expectStatus().isNoContent()
            .expectBody().isEmpty();
        
        client.get()
            .uri(BaseApiConstants.USER_BASE_URL_V1.concat("/{id}"), Collections.singletonMap("id", user.getId()))
            .exchange()
            .expectStatus().isNotFound()
            .expectBody().isEmpty();
    }
}
