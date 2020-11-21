package com.omblanco.springboot.webflux.api.app.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.omblanco.springboot.webflux.api.app.model.entity.User;
import com.omblanco.springboot.webflux.api.app.model.repository.UserRepository;
import com.omblanco.springboot.webflux.api.app.web.dto.UserDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Tests unitarios del UserService
 * @author oscar.martinezblanco
 *
 */
public class UserServiceImplTests {

    @Mock
    private UserRepository mockUserRepository;
    
    @Mock
    private ModelMapper modelMapper;
    
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    
    private UserService userService;
    
    @BeforeEach
    public void setUp() {
        initMocks(this);
        userService = new UserServiceImpl(mockUserRepository, modelMapper, passwordEncoder);
    }
    
    @Test
    public void findAllTest() {
        //Given:
        User user1 = new User(1L, "John", "Doe", "john@mail.com", new Date(), "1234");
        User user2 = new User(2L, "Mary", "Queen", "mary@mail.com", new Date(), "1234");
        List<User> users = Arrays.asList(user1, user2);
        
        UserDTO userDto1 = new UserDTO(1L, "John", "Doe", "john@mail.com", new Date(), "1234");
        UserDTO userDto2 = new UserDTO(2L, "Mary", "Queen", "mary@mail.com", new Date(), "1234");
        
        //when:
        when(modelMapper.map(user1, UserDTO.class)).thenReturn(userDto1);
        when(modelMapper.map(user2, UserDTO.class)).thenReturn(userDto2);
        when(mockUserRepository.findAll()).thenReturn(users);
        
        Flux<UserDTO> fluxUsersDto = userService.findAll();

        //Then:
        StepVerifier.create(fluxUsersDto.log()).expectNextCount(2).verifyComplete();
        StepVerifier.create(fluxUsersDto.log()).expectNext(userDto1).expectNext(userDto2).verifyComplete();
        StepVerifier.create(fluxUsersDto.log()).expectNext(userDto1, userDto2).verifyComplete();
    }
    
    @Test
    public void findByIdTest() {
        //Given:
        User user = new User(1L, "John", "Doe", "john@mail.com", new Date(), "1234");
        
        UserDTO userDto = new UserDTO(1L, "John", "Doe", "john@mail.com", new Date(), "1234");
        
        //when:
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDto);
        when(mockUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mockUserRepository.findById(2L)).thenReturn(Optional.empty());
        
        Mono<UserDTO> monoUser = userService.findById(user.getId());
        Mono<UserDTO> monoVoid = userService.findById(2L);

        //Then:
        StepVerifier.create(monoUser.log()).expectNext(userDto).verifyComplete();
        StepVerifier.create(monoVoid.log()).verifyComplete();
    }
    
    @Test
    public void saveTest() {
        //Given:
        UserDTO userDto = new UserDTO(1L, "John", "Doe", "john@mail.com", new Date(), "1234");
        User user = new User(1L, "John", "Doe", "john@mail.com", new Date(), "1234");
        
        //when:
        when(mockUserRepository.save(user)).thenReturn(user);
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDto);
        
        //Then:
        Mono<UserDTO> monoUser = userService.save(userDto);
        
        StepVerifier.create(monoUser.log()).expectNext(userDto).verifyComplete();
    }
    
    @Test
    public void deleteTest() {
        //Given:
        User user = new User(1L, "John", "Doe", "john@mail.com", new Date(), "1234");
        UserDTO userDto = new UserDTO(1L, "John", "Doe", "john@mail.com", new Date(), "1234");
        
        //when:
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        doNothing().when(mockUserRepository).delete(user);
        Mono<Void> monoUser = userService.delete(userDto);
        
        //Then:
        StepVerifier.create(monoUser.log()).verifyComplete();
    }
}
