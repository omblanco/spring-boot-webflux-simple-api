package com.omblanco.springboot.webflux.api.mongo.app.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.omblanco.springboot.webflux.api.commons.services.CommonService;
import com.omblanco.springboot.webflux.api.commons.web.dto.UserFilterDTO;
import com.omblanco.springboot.webflux.api.mongo.app.model.entity.User;
import com.omblanco.springboot.webflux.api.mongo.app.web.dtos.UserDTO;

import reactor.core.publisher.Mono;

public interface UserService extends CommonService<UserDTO, User, String>{

    /**
     * Recupera usuarios paginados y filtrados
     * @param filter Filtro de búsqueda
     * @param pageable Paginación
     * @return Mono de página de usuarios
     */
    Mono<Page<UserDTO>> findByFilter(UserFilterDTO filter, Pageable pageable);
    
    /**
     * Busca un usuario por email
     * @param email Email
     * @return Usuario
     */
    Mono<UserDTO> findByEmail(String email);
    
}
