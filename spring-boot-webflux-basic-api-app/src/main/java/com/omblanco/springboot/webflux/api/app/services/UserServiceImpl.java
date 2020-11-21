package com.omblanco.springboot.webflux.api.app.services;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.omblanco.springboot.webflux.api.app.model.entity.User;
import com.omblanco.springboot.webflux.api.app.model.repository.UserRepository;
import com.omblanco.springboot.webflux.api.app.model.specifications.UserSpecifications;
import com.omblanco.springboot.webflux.api.app.web.dto.UserDTO;
import com.omblanco.springboot.webflux.api.app.web.dto.UserFilterDTO;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Implmentación del servicio de usuarios
 * @author oscar.martinezblanco
 *
 */
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    
    private ModelMapper modelMapper;
    
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Flux<UserDTO> findAll() {
        return Flux.defer(() -> Flux.fromIterable(userRepository.findAll()
                .stream().map(this::convertToDto)
                .collect(Collectors.toList())))
                .subscribeOn(Schedulers.boundedElastic());
    }
    
    @Override
    public Mono<Page<UserDTO>> findByFilter(UserFilterDTO filter, Pageable pageable) {
        return Mono.defer(() -> Mono.just(userRepository.findAll(UserSpecifications.withFilter(filter), pageable)))
            .map(this::convertPageToDto)
            .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UserDTO> findById(Long id) {
        
        return Mono.defer(() -> Mono.just(userRepository.findById(id))).flatMap(optional -> {
            if (optional.isPresent()) {
                return Mono.just(convertToDto(optional.get()));
            }
            
            return Mono.empty();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UserDTO> save(UserDTO userDto) {
        
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        return Mono.defer(() -> Mono.just(convertToDto(userRepository.save(convertToEntity(userDto)))))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> delete(UserDTO userDto) {
        return Mono.defer(() -> {
            userRepository.delete(convertToEntity(userDto));
            return Mono.empty();
        }).subscribeOn(Schedulers.boundedElastic()).then();
    } 
    
    @Override
    public Mono<UserDTO> findByEmail(String email) {
        return Mono.defer(() -> Mono.just(userRepository.findByEmail(email))).flatMap(optional -> {
            if (optional.isPresent()) {
                return Mono.just(convertToDto(optional.get()));
            }
            
            return Mono.empty();
        }).subscribeOn(Schedulers.boundedElastic());
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
     * Transforma una página de modelos a dtos
     * @param userPage Página de modelos
     * @return Página de dtos
     */
    private Page<UserDTO> convertPageToDto(Page<User> userPage) {
        return new PageImpl<UserDTO>(userPage.getContent().stream().map(user -> {
            return this.convertToDto(user);
        }).collect(Collectors.toList()), userPage.getPageable(), userPage.getSize());
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
