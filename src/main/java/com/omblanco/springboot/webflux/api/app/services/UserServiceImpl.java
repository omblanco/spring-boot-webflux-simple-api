package com.omblanco.springboot.webflux.api.app.services;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.omblanco.springboot.webflux.api.app.model.entity.User;
import com.omblanco.springboot.webflux.api.app.model.repository.UserRepository;
import com.omblanco.springboot.webflux.api.app.web.dto.UserDTO;

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
    
    private ModelMapper modelMapper;

    @Override
    public Flux<UserDTO> findAll() {
        return Flux.defer(() -> Flux.fromIterable(userRepository.findAll()
                .stream().map(this::convertToDto)
                .collect(Collectors.toList())))
                .subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<UserDTO> findById(Long id) {
        
        return Mono.defer(() -> Mono.just(userRepository.findById(id))).flatMap(optional -> {
            if (optional.isPresent()) {
                return Mono.just(convertToDto(optional.get()));
            }
            
            return Mono.empty();
        }).subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<UserDTO> save(UserDTO userDto) {
        
        return Mono.defer(() -> Mono.just(convertToDto(userRepository.save(convertToEntity(userDto)))))
                .subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<Void> delete(UserDTO userDto) {
        return Mono.defer(() -> {
            userRepository.delete(convertToEntity(userDto));
            return Mono.empty();
        }).subscribeOn(jdbcScheduler).then();
    } 
    
    /**
     * Conversión de Modelo a DTO
     * @param user Usuario Modelo
     * @return Usuario DTO
     */
    private UserDTO convertToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
    
    /**
     * Conversión de DTO a Modelo
     * @param userDto DTO del usuario
     * @return Modelo del usuario
     */
    private User convertToEntity(UserDTO userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
