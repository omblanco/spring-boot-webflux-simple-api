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
import com.omblanco.springboot.webflux.api.commons.services.CommonServiceImpl;
import com.omblanco.springboot.webflux.api.commons.web.dto.UserFilterDTO;

import lombok.Builder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Implmentación del servicio de usuarios
 * @author oscar.martinezblanco
 *
 */
@Service
public class UserServiceImpl extends CommonServiceImpl<UserDTO, User, UserRepository, Long> implements UserService {

    private ModelMapper modelMapper;
    
    private BCryptPasswordEncoder passwordEncoder;
    
    @Builder
    public UserServiceImpl(UserRepository repository, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder) {
        super(repository);
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<Page<UserDTO>> findByFilter(UserFilterDTO filter, Pageable pageable) {
        return Mono.defer(() -> Mono.just(repository.findAll(UserSpecifications.withFilter(filter), pageable)))
            .map(this::convertPageToDto)
            .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UserDTO> save(UserDTO userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return super.save(userDto);
    }

    @Override
    public Mono<UserDTO> findByEmail(String email) {
        return Mono.defer(() -> Mono.just(repository.findByEmail(email))).flatMap(optional -> {
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
    @Override 
    protected UserDTO convertToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
    
    /**
     * Transforma una página de modelos a dtos
     * @param userPage Página de modelos
     * @return Página de dtos
     */
    @Override 
    protected Page<UserDTO> convertPageToDto(Page<User> userPage) {
        return new PageImpl<UserDTO>(userPage.getContent().stream().map(user -> {
            return this.convertToDto(user);
        }).collect(Collectors.toList()), userPage.getPageable(), userPage.getTotalElements());
    }
    
    /**
     * Conversión de DTO a Modelo
     * @param userDto DTO del usuario
     * @return Modelo del usuario
     */
    @Override
    protected User convertToEntity(UserDTO userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
