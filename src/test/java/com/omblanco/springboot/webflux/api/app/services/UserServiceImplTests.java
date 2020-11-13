package com.omblanco.springboot.webflux.api.app.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.omblanco.springboot.webflux.api.app.model.entity.User;
import com.omblanco.springboot.webflux.api.app.model.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.test.StepVerifier;

/**
 * Tests unitarios del UserService
 * @author oscar.martinezblanco
 *
 */
@Disabled
public class UserServiceImplTests {

    @Mock
    private UserRepository mockUserRepository;
    
    @Mock
    private Scheduler jdbcScheduler;
    
    private UserService userService;
    
    @BeforeEach
    public void setUp() {
        initMocks(this);
        userService = new UserServiceImpl(mockUserRepository, jdbcScheduler);
    }
    
    @Test
    public void findAllTest() {
        //Given:
        User user1 = new User(1L, "John", "Doe", "john@mail.com", new Date());
        User user2 = new User(1L, "Mary", "Queen", "mary@mail.com", new Date());
        List<User> users = Arrays.asList(user1, user2);
        
        //when:
        when(mockUserRepository.findAll()).thenReturn(users);
        
        //Then:
        Flux<User> fluxUsers = userService.findAll();
        
        StepVerifier.create(fluxUsers.log()).expectNextCount(2).verifyComplete();
        StepVerifier.create(fluxUsers.log()).expectNext(user1).expectNext(user2).verifyComplete();
        StepVerifier.create(fluxUsers.log()).expectNext(user1, user2).verifyComplete();
    }
    
    @Test
    public void findByIdTest() {
        //Given:
        User user = new User(1L, "John", "Doe", "john@mail.com", new Date());
        
        //when:
        when(mockUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mockUserRepository.findById(2L)).thenReturn(Optional.empty());
        
        //Then:
        Mono<User> monoUser = userService.findById(user.getId());
        Mono<User> monoVoid = userService.findById(2L);
        
        StepVerifier.create(monoUser.log()).expectNext(user).verifyComplete();
        StepVerifier.create(monoVoid.log()).verifyComplete();
    }
    
    @Test
    public void saveTest() {
        //Given:
        User user = new User(1L, "John", "Doe", "john@mail.com", new Date());
        
        //when:
        when(mockUserRepository.save(user)).thenReturn(user);
        
        //Then:
        Mono<User> monoUser = userService.save(user);
        
        StepVerifier.create(monoUser.log()).expectNext(user).verifyComplete();
    }
    
    @Test
    public void deleteTest() {
        //Given:
        User user = new User(1L, "John", "Doe", "john@mail.com", new Date());
        
        //when:
        doNothing().when(mockUserRepository).delete(user);
        
        //Then:
        Mono<Void> monoUser = userService.delete(user);
        
        StepVerifier.create(monoUser.log()).verifyComplete();
    }
}
