package com.omblanco.springboot.webflux.api.app.web.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.omblanco.springboot.webflux.api.app.ModelMapperConfig;
import com.omblanco.springboot.webflux.api.app.model.entity.User;
import com.omblanco.springboot.webflux.api.app.model.repository.UserRepository;
import com.omblanco.springboot.webflux.api.app.services.UserServiceImpl;
import com.omblanco.springboot.webflux.api.app.utils.BaseApiConstants;

import reactor.core.publisher.Mono;


@Disabled
@WebFluxTest(controllers = UserController.class)
@Import({UserServiceImpl.class, ModelMapperConfig.class})
public class UserControllerUnitTest {
    
    @MockBean
    private UserRepository userRepository;
    
    @Autowired
    WebTestClient webTestClient;
    
    @Test
    public void findAllTest() throws Exception {
        //given:
        User user1 = new User(1L, "John", "Doe", "john@mail.com", new Date());
        User user2 = new User(1L, "Mary", "Queen", "mary@mail.com", new Date());
        
        //when:
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        webTestClient.get().uri(BaseApiConstants.USER_BASE_URL_V1)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(User.class)
            .consumeWith(response -> {
                List<User> users = response.getResponseBody();
                users.forEach(user -> {
                    System.out.println(user);
                });
                
                //then:
                assertThat(users.size() == 2).isTrue();
                assertThat(user1).isEqualTo(users.get(0));
                assertThat(user2).isEqualTo(users.get(1));
            });
        
        //then:
        verify(userRepository, times(1)).findAll();
    }
    
    @Test
    public void getByidTest() {
        //given:
        User user = new User(1L, "John", "Doe", "john@mail.com", new Date());
        Optional<User> optionalUser = Optional.of(user);
        
        //when:
        when(userRepository.findById(user.getId())).thenReturn(optionalUser);
        
        //then:
        webTestClient.get()
        .uri(BaseApiConstants.USER_BASE_URL_V1.concat("/{id}"), Collections.singletonMap("id", user.getId()))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(User.class)
        .consumeWith(response -> {
            
            //then:
            User userResponse = response.getResponseBody();
            assertThat(userResponse.getId()).isNotNull();
            assertThat(userResponse.getId() > 0).isTrue();
            assertThat(userResponse.getId()).isEqualTo(user.getId());
            assertThat(userResponse.getBirthdate()).isEqualTo(user.getBirthdate());
            assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
            assertThat(userResponse.getName()).isEqualTo(user.getName());
            assertThat(userResponse.getSurname()).isEqualTo(user.getSurname());
        });
        
        //then:
        verify(userRepository, times(1)).findById(user.getId());
    }
    
    @Test
    public void postTest() {
        //given:
        User user = new User(1L, "John", "Doe", "john@mail.com", new Date());
        
        //when
        when(userRepository.save(user)).thenReturn(user);
        webTestClient.post()
        .uri(BaseApiConstants.USER_BASE_URL_V1)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(user), User.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(User.class).consumeWith(response -> {
            
            //then:
            User userResponse = response.getResponseBody();
            Assertions.assertThat(userResponse.getId()).isNotNull();
            Assertions.assertThat(userResponse.getId() > 0).isTrue();
            Assertions.assertThat(userResponse.getBirthdate()).isEqualTo(user.getBirthdate());
            Assertions.assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
            Assertions.assertThat(userResponse.getName()).isEqualTo(user.getName());
            Assertions.assertThat(userResponse.getSurname()).isEqualTo(user.getSurname());
        });
        
        //then:
        verify(userRepository, times(1)).save(user);
    }
    
    @Test
    public void putTest() {
        //given:
        User user = new User(1L, "John", "Doe", "john@mail.com", new Date());
        Optional<User> optionalUser = Optional.of(user);
        
        //when:
        when(userRepository.findById(user.getId())).thenReturn(optionalUser);
        when(userRepository.save(user)).thenReturn(user);
       
        webTestClient.put()
        .uri(BaseApiConstants.USER_BASE_URL_V1.concat("/{id}"), Collections.singletonMap("id", user.getId()))
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(user), User.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(User.class)
        .consumeWith(response -> {
            //then:
            User userResponse = response.getResponseBody();
            Assertions.assertThat(userResponse.getId()).isNotNull();
            Assertions.assertThat(userResponse.getId() > 0).isTrue();
            Assertions.assertThat(userResponse.getId()).isEqualTo(user.getId());
            Assertions.assertThat(userResponse.getBirthdate()).isEqualTo(user.getBirthdate());
            Assertions.assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
            Assertions.assertThat(userResponse.getName()).isEqualTo(user.getName());
            Assertions.assertThat(userResponse.getSurname()).isEqualTo(user.getSurname());
        });
        
        //then:
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(user);
    }
    
    @Test
    public void deleteTest() {
        //given:
        User user = new User(1L, "John", "Doe", "john@mail.com", new Date());
        Optional<User> optionalUser = Optional.of(user);
        
        //when:
        when(userRepository.findById(user.getId())).thenReturn(optionalUser);
        when(userRepository.save(user)).thenReturn(user);
        webTestClient.delete()
            .uri(BaseApiConstants.USER_BASE_URL_V1.concat("/{id}"), Collections.singletonMap("id", user.getId()))
            .exchange()
            .expectStatus().isNoContent()
            .expectBody().isEmpty();
        
        //then:
        verify(userRepository, times(1)).delete(user);
    }
}
