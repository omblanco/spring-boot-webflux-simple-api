package com.omblanco.springboot.webflux.api.app.web.controllers;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.omblanco.springboot.webflux.api.app.services.UserService;
import com.omblanco.springboot.webflux.api.app.utils.BaseApiConstants;
import com.omblanco.springboot.webflux.api.app.web.dto.UserDTO;

import reactor.core.publisher.Mono;

/**
 * Test de integración para el UserController
 * @author oscar.martinezblanco
 *
 */
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
        .expectBodyList(UserDTO.class)
        .consumeWith(response -> {
            List<UserDTO> users = response.getResponseBody();
            users.forEach(user -> {
                System.out.println(user);
            });
            
            Assertions.assertThat(users.size() > 0).isTrue();
        });
    }
    
    @Test
    public void findFyFilterTest() {
        String name = "Maria";
        Integer page = 0;
        Integer size = 10;
        
        client.get().uri(uriBuilder ->
            uriBuilder
            .path(BaseApiConstants.USER_BASE_URL_V1)
            .queryParam("page", page)
            .queryParam("size", size)
            .queryParam("name", name)
            .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange().expectStatus()
        .isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.numberOfElements").isNotEmpty()
        .jsonPath("$.numberOfElements").isEqualTo(1)
        .jsonPath("$.content").isArray()
        .jsonPath("$.content.length()").isEqualTo(1)
        .jsonPath("$.content[0].name").isEqualTo(name);
    }
    
    @Test
    public void getByidTest() {
        UserDTO user = userService.findAll().blockFirst();
        
        client.get()
        .uri(BaseApiConstants.USER_BASE_URL_V1.concat("/{id}"), Collections.singletonMap("id", user.getId()))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(UserDTO.class)
        .consumeWith(response -> {
            UserDTO userResponse = response.getResponseBody();
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
        UserDTO user = new UserDTO(null, "Fulano", "De tal", "fulano@mail.com", new Date());
        
        client.post()
        .uri(BaseApiConstants.USER_BASE_URL_V1)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(user), UserDTO.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(UserDTO.class).consumeWith(response -> {
            UserDTO userResponse = response.getResponseBody();
            Assertions.assertThat(userResponse.getId()).isNotNull();
            Assertions.assertThat(userResponse.getId() > 0).isTrue();
            Assertions.assertThat(userResponse.getBirthdate()).isEqualTo(user.getBirthdate());
            Assertions.assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
            Assertions.assertThat(userResponse.getName()).isEqualTo(user.getName());
            Assertions.assertThat(userResponse.getSurname()).isEqualTo(user.getSurname());
        });
    }
    
    @Test
    public void postWithValidationErrorsTest() {
        UserDTO user = new UserDTO(null, "F", "De", "fu", null);
        
        client.post()
        .uri(BaseApiConstants.USER_BASE_URL_V1)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(user), UserDTO.class)
        .exchange()
        .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.code").isNotEmpty()
        .jsonPath("$.code").isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.name())
        .jsonPath("$.message").isNotEmpty()
        .jsonPath("$.message").isEqualTo("Validation failure: userDTO")
        .jsonPath("$.errors").isArray()
        .jsonPath("$.errors.length()").isEqualTo(5);
    }
    
    @Test
    public void putTest() {
        UserDTO user = userService.findAll().blockFirst();
        
        user.setEmail("email@mail.com");
        user.setName("Name");
        user.setSurname("Surname");
        
        client.put()
        .uri(BaseApiConstants.USER_BASE_URL_V1.concat("/{id}"), Collections.singletonMap("id", user.getId()))
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(user), UserDTO.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(UserDTO.class)
        .consumeWith(response -> {
            UserDTO userResponse = response.getResponseBody();
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
        UserDTO user = userService.findAll().blockFirst();
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
