package com.omblanco.springboot.webflux.api.mongo.app.services;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.omblanco.springboot.webflux.api.commons.services.CommonReactiveServiceImpl;
import com.omblanco.springboot.webflux.api.commons.web.dto.UserFilterDTO;
import com.omblanco.springboot.webflux.api.mongo.app.model.entity.User;
import com.omblanco.springboot.webflux.api.mongo.app.model.repositories.UserRepository;
import com.omblanco.springboot.webflux.api.mongo.app.web.dtos.UserDTO;

import lombok.Builder;
import reactor.core.publisher.Mono;

/**
 * Implmentación del servicio de usuarios
 * @author oscar.martinezblanco
 *
 */
@Service
public class UserServiceImpl extends CommonReactiveServiceImpl<UserDTO, User, UserRepository, String> implements UserService {

    private ModelMapper modelMapper;
    
    private BCryptPasswordEncoder passwordEncoder;
        
    
    @Builder
    public UserServiceImpl(UserRepository repository, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder) {
        super(repository);
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public Mono<UserDTO> save(UserDTO dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return super.save(dto);
    }

    @Override
    public Mono<Page<UserDTO>> findByFilter(UserFilterDTO filter, Pageable pageable) {
        return repository.countBy(filter).flatMap(count -> {
            return repository.findBy(filter, pageable).collect(Collectors.toList()).flatMap(users -> {
                return Mono.just(convertPageToDto(new PageImpl<User>(users, pageable, count)));
            });
        });        
    }

    @Override
    public Mono<UserDTO> findByEmail(String email) {
        return repository.findByEmail(email).map(this::convertToDto);
    }

    @Override
    protected UserDTO convertToDto(User entity) {
        return modelMapper.map(entity, UserDTO.class);
    }

    @Override
    protected Page<UserDTO> convertPageToDto(Page<User> entityPage) {
        return new PageImpl<UserDTO>(entityPage.getContent().stream().map(user -> {
            return this.convertToDto(user);
        }).collect(Collectors.toList()), entityPage.getPageable(), entityPage.getTotalElements());
    }

    @Override
    protected User convertToEntity(UserDTO dto) {
        return modelMapper.map(dto, User.class);
    }
}
