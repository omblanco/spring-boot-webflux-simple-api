package com.omblanco.springboot.webflux.api.app.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.omblanco.springboot.webflux.api.app.model.entity.User;
import com.omblanco.springboot.webflux.api.app.model.repository.UserRepository;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

/**
 * Implmentación del servicio de usuarios
 * @author oscar.martinezblanco
 *
 */
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    
    private Scheduler jdbcScheduler;

    @Override
    public Flux<User> findAll() {
        return Flux.defer(() -> Flux.fromIterable(userRepository.findAll()))
                .subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<User> findById(Long id) {
        
        return Mono.defer(() -> Mono.just(userRepository.findById(id))).flatMap(optional -> {
            if (optional.isPresent()) {
                return Mono.just(optional.get());
            }
            
            return Mono.empty();
        }).subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<User> save(User user) {
        
        return Mono.defer(() -> Mono.just(userRepository.save(user)))
                .subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<Void> delete(User user) {
        return Mono.defer(() -> {
            userRepository.delete(user);
            return Mono.empty();
        }).subscribeOn(jdbcScheduler).then();
    } 
}
