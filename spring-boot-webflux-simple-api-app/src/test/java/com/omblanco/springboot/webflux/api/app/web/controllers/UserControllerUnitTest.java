package com.omblanco.springboot.webflux.api.app.web.controllers;

import static com.omblanco.springboot.webflux.api.commons.utils.BaseApiConstants.USER_BASE_URL_V1;
import static com.omblanco.springboot.webflux.api.commons.utils.BaseApiConstants.USER_BASE_URL_V2;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.omblanco.springboot.webflux.api.app.configuration.ModelMapperConfig;
import com.omblanco.springboot.webflux.api.app.configuration.SecurityConfig;
import com.omblanco.springboot.webflux.api.app.configuration.SecurityWebFilterChainConfig;
import com.omblanco.springboot.webflux.api.app.model.entity.User;
import com.omblanco.springboot.webflux.api.app.model.repository.UserRepository;
import com.omblanco.springboot.webflux.api.app.services.UserServiceImpl;
import com.omblanco.springboot.webflux.api.app.web.dto.UserDTO;

import reactor.core.publisher.Mono;

/**
 * Test unitarios para los controllers
 * @author oscar.martinezblanco
 *
 *  see https://www.baeldung.com/parameterized-tests-junit-5
 *
 */
@WebFluxTest(controllers = {UserController.class, UserRestController.class})
@Import({UserServiceImpl.class, ModelMapperConfig.class, SecurityConfig.class, SecurityWebFilterChainConfig.class})
public class UserControllerUnitTest {
    
    @MockBean
    private UserRepository userRepository;
    
    @MockBean
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    WebTestClient webTestClient;
    
    @ParameterizedTest
    @ValueSource(strings = {USER_BASE_URL_V1, USER_BASE_URL_V2})
    public void findAllTest(String path) throws Exception {
        //given:
        User user1 = new User(1L, "John", "Doe", "john@mail.com", new Date(), "1234");
        User user2 = new User(1L, "Mary", "Queen", "mary@mail.com", new Date(), "1234");
        
        //when:
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        webTestClient.get().uri(path)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(UserDTO.class)
            .consumeWith(response -> {
                List<UserDTO> users = response.getResponseBody();
                users.forEach(user -> {
                    System.out.println(user);
                });
                
                //then:
                assertThat(users.size() == 2).isTrue();
                assertThat(user1.getId()).isEqualTo(users.get(0).getId());
                assertThat(user1.getName()).isEqualTo(users.get(0).getName());
                assertThat(user1.getSurname()).isEqualTo(users.get(0).getSurname());
                assertThat(user2.getId()).isEqualTo(users.get(1).getId());
                assertThat(user2.getName()).isEqualTo(users.get(1).getName());
                assertThat(user2.getSurname()).isEqualTo(users.get(1).getSurname());
            });
        
        //then:
        verify(userRepository, times(1)).findAll();
    }
    
    @ParameterizedTest
    @ValueSource(strings = {USER_BASE_URL_V1, USER_BASE_URL_V2})
    @SuppressWarnings("unchecked")
    public void findFyFilterTest(String path) {
        //given
        User user = new User(1L, "Maria", "Doe", "john@mail.com", new Date(), "1234");
        
        String name = "Maria";
        Integer page = 0;
        Integer size = 10;
        
        Page<User> pageUsers = new PageImpl<User>(Arrays.asList(user));
        
        //when
        when(userRepository.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.any())).thenReturn(pageUsers);
        webTestClient.get().uri(uriBuilder ->
            uriBuilder
            .path(path)
            .queryParam("page", page)
            .queryParam("size", size)
            .queryParam("name", name)
            .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange().expectStatus()
        .isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
         //then:
        .jsonPath("$.numberOfElements").isNotEmpty()
        .jsonPath("$.numberOfElements").isEqualTo(1)
        .jsonPath("$.content").isArray()
        .jsonPath("$.content.length()").isEqualTo(1)
        .jsonPath("$.content[0].name").isEqualTo(name);
        
        //then:
        verify(userRepository, times(1)).findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.any());
    }    
    
    @ParameterizedTest
    @ValueSource(strings = {USER_BASE_URL_V1, USER_BASE_URL_V2})
    public void getByidTest(String path) {
        //given:
        User user = new User(1L, "John", "Doe", "john@mail.com", new Date(), "1234");
        Optional<User> optionalUser = Optional.of(user);
        
        //when:
        when(userRepository.findById(user.getId())).thenReturn(optionalUser);
        
        //then:
        webTestClient.get()
        .uri(path.concat("/{id}"), Collections.singletonMap("id", user.getId()))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(UserDTO.class)
        .consumeWith(response -> {
            
            //then:
            UserDTO userResponse = response.getResponseBody();
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
    
    
    @ParameterizedTest
    @ValueSource(strings = {USER_BASE_URL_V1, USER_BASE_URL_V2})
    public void postTest(String path) {
        //given:
        UserDTO userDtoRequest = new UserDTO(1L, "John", "Doe", "john@mail.com", new Date(), "1234");
        User user = new User(1L, "John", "Doe", "john@mail.com", new Date(), "1234");
        
        //when
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(userDtoRequest.getPassword())).thenReturn("encodePassword");
        webTestClient.post()
        .uri(path)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(userDtoRequest), UserDTO.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(UserDTO.class).consumeWith(response -> {
            
            //then:
            UserDTO userResponse = response.getResponseBody();
            Assertions.assertThat(userResponse.getId()).isNotNull();
            Assertions.assertThat(userResponse.getId() > 0).isTrue();
            Assertions.assertThat(userResponse.getBirthdate()).isEqualTo(user.getBirthdate());
            Assertions.assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
            Assertions.assertThat(userResponse.getName()).isEqualTo(user.getName());
            Assertions.assertThat(userResponse.getSurname()).isEqualTo(user.getSurname());
        });
        
        //then:
        verify(userRepository, times(1)).save(ArgumentMatchers.any(User.class));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {USER_BASE_URL_V1, USER_BASE_URL_V2})
    public void putTest(String path) {
        //given:
        UserDTO userDtoRequest = new UserDTO(1L, "John", "Doe", "john@mail.com", new Date(), "1234");
        User user = new User(1L, "John", "Doe", "john@mail.com", new Date(), "1234");
        Optional<User> optionalUser = Optional.of(user);
        
        //when:
        when(userRepository.findById(user.getId())).thenReturn(optionalUser);
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodePassword");
       
        webTestClient.put()
        .uri(path.concat("/{id}"), Collections.singletonMap("id", user.getId()))
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(userDtoRequest), UserDTO.class)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(UserDTO.class)
        .consumeWith(response -> {
            //then:
            UserDTO userResponse = response.getResponseBody();
            Assertions.assertThat(userResponse.getId()).isNotNull();
            Assertions.assertThat(userResponse.getId() > 0).isTrue();
            Assertions.assertThat(userResponse.getId()).isEqualTo(user.getId());
            Assertions.assertThat(userResponse.getBirthdate()).isEqualTo(user.getBirthdate());
            Assertions.assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
            Assertions.assertThat(userResponse.getName()).isEqualTo(user.getName());
            Assertions.assertThat(userResponse.getSurname()).isEqualTo(user.getSurname());
            Assertions.assertThat(userResponse.getPassword()).isEqualTo(user.getPassword());
        });
        
        //then:
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(ArgumentMatchers.any(User.class));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {USER_BASE_URL_V1, USER_BASE_URL_V2})
    public void deleteTest(String path) {
        //given:
        User user = new User(1L, "John", "Doe", "john@mail.com", new Date(), "1234");
        Optional<User> optionalUser = Optional.of(user);
        
        //when:
        when(userRepository.findById(user.getId())).thenReturn(optionalUser);
        when(userRepository.save(user)).thenReturn(user);
        webTestClient.delete()
            .uri(path.concat("/{id}"), Collections.singletonMap("id", user.getId()))
            .exchange()
            .expectStatus().isNoContent()
            .expectBody().isEmpty();
        
        //then:
        verify(userRepository, times(1)).delete(user);
    }
}
