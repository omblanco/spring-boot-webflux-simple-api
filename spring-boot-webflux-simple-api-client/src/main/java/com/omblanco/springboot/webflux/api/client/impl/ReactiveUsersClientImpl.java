package com.omblanco.springboot.webflux.api.client.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.omblanco.springboot.webflux.api.client.ReactiveUsersClient;
import com.omblanco.springboot.webflux.api.client.dto.RestResponsePage;
import com.omblanco.springboot.webflux.api.client.dto.UserDTO;
import com.omblanco.springboot.webflux.api.client.dto.UserFilterDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class ReactiveUsersClientImpl<K> implements ReactiveUsersClient<K> {
	
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
    public Flux<UserDTO<K>> getAllUsers() {
        return this.client.get()
                .uri(String.format(USER_BASE_URL, this.version))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMapMany(response -> response.bodyToFlux(new ParameterizedTypeReference<UserDTO<K>>(){}));
    }

    @Override
    public Mono<UserDTO<K>> get(K id) {
        return this.client.get()
                .uri(String.format(USER_BASE_URL_WITH_ID_PARAM, this.version), Collections.singletonMap(PARAM_ID_NAME, id))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<UserDTO<K>>(){});
    }
    
    @Override
    public Mono<UserDTO<K>> save(UserDTO<K> user) {
        return client.post()
                .uri(String.format(USER_BASE_URL, this.version))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(user))
                .retrieve().bodyToMono(new ParameterizedTypeReference<UserDTO<K>>(){});
    }

    @Override
    public Mono<UserDTO<K>> update(UserDTO<K> user, K id) {
        return client.post().uri(String.format(USER_BASE_URL_WITH_ID_PARAM, this.version), Collections.singletonMap("id", id))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(user))
                .retrieve().bodyToMono(new ParameterizedTypeReference<UserDTO<K>>(){});
    }

    @Override
    public Mono<Void> delete(K id) {
        return client.delete()
                .uri(String.format(USER_BASE_URL_WITH_ID_PARAM, this.version), Collections.singletonMap("id", id))
                .retrieve()
                .bodyToMono(Void.class);
    }

	@Override
	public Flux<UserDTO<K>> getUsers(UserFilterDTO filter, Sort sort) {

        List<String> sorts = new ArrayList<>();

        if (sort != null) {
            sort.forEach(order -> sorts.add(String.join(",", order.getProperty(), order.getDirection().toString())));
        }

        return this.client.get()
                .uri(builder -> builder.path(String.format(USER_BASE_URL, this.version))
                        .queryParam("email", filter.getEmail())
                        .queryParam("name", filter.getName())
                        .queryParam("surname", filter.getSurname())
                        .queryParam("sort", sorts.toArray())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMapMany(response -> response.bodyToFlux(new ParameterizedTypeReference<UserDTO<K>>(){}));
	}

    @Override
    public Mono<RestResponsePage<UserDTO<K>>> getUsers(UserFilterDTO filter, Pageable pageable) {
        List<String> sorts = new ArrayList<>();

        
        
        if (pageable.getSort() != null) {
            pageable.getSort().forEach(order -> sorts.add(String.join(",", order.getProperty(), order.getDirection().toString())));
        }

        return this.client.get()
                .uri(builder -> builder.path(String.format(USER_BASE_URL, this.version))
                        .queryParam("email", filter.getEmail())
                        .queryParam("name", filter.getName())
                        .queryParam("surname", filter.getSurname())
                        .queryParam("sort", sorts.toArray())
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<RestResponsePage<UserDTO<K>>>(){});
    }

}
