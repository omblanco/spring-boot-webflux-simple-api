package com.omblanco.springboot.webflux.api.client.impl;

import java.util.Collections;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.omblanco.springboot.webflux.api.client.ReactiveUsersClient;
import com.omblanco.springboot.webflux.api.client.dto.UserDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class ReactiveUsersClientImpl implements ReactiveUsersClient {

    //TODO:authentication
    private final String user;
    private final String password;
    private final String endpoint;
    private String version = "3";
    
    private static final String USER_BASE_URL = "/api/v%s/users";
    private static final String USER_BASE_URL_WITH_ID_PARAM = USER_BASE_URL.concat("/{id}");
    private static final String PARAM_ID_NAME = "id";
    
    private final WebClient client;
    
    public ReactiveUsersClientImpl(String user, String password, String endpoint) {
        this.user = user;
        this.password = password;
        this.endpoint = endpoint;
        this.client = WebClient.create(endpoint);
    }
    
    public ReactiveUsersClientImpl(String user, String password, String endpoint, String version) {
        this(user, password, endpoint);
        this.version = version;
    }

    @Override
    public Flux<UserDTO> getAllUsers() {
        return this.client.get()
                .uri(String.format(USER_BASE_URL, this.version))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMapMany(response -> response.bodyToFlux(UserDTO.class));
    }

    @Override
    public Mono<UserDTO> get(Long id) {
        return this.client.get()
                .uri(String.format(USER_BASE_URL_WITH_ID_PARAM, this.version), Collections.singletonMap(PARAM_ID_NAME, id))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(UserDTO.class);
    }
    
    @Override
    public Mono<UserDTO> save(UserDTO user) {
        return client.post()
                .uri(String.format(USER_BASE_URL, this.version))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(user))
                .retrieve().bodyToMono(UserDTO.class);
    }

    @Override
    public Mono<UserDTO> update(UserDTO user, Long id) {
        return client.post().uri(String.format(USER_BASE_URL_WITH_ID_PARAM, this.version), Collections.singletonMap("id", id))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(user))
                .retrieve().bodyToMono(UserDTO.class);
    }

    @Override
    public Mono<Void> delete(String id) {
        return client.delete()
                .uri(String.format(USER_BASE_URL_WITH_ID_PARAM, this.version), Collections.singletonMap("id", id))
                .retrieve()
                .bodyToMono(Void.class);
    }

}
