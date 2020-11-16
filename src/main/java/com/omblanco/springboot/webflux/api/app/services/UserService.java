package com.omblanco.springboot.webflux.api.app.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.omblanco.springboot.webflux.api.app.web.dto.UserDTO;
import com.omblanco.springboot.webflux.api.app.web.dto.UserFilterDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz del servicio de usuario
 * @author oscar.martinezblanco
 *
 */
public interface UserService {

    /**
     * Recupera todos los Usuarios
     * @return Flux de usuarios
     */
    Flux<UserDTO> findAll();
    
    /**
     * Recupera usuarios paginados y filtrados
     * @param filter Filtro de búsqueda
     * @param pageable Paginación
     * @return Mono de página de usuarios
     */
    Mono<Page<UserDTO>> findByFilter(UserFilterDTO filter, Pageable pageable);
    
    /**
     * Busca un usuario por la clave
     * @param id Clave
     * @return Usuario
     */
    Mono<UserDTO> findById(Long id);
    
    /**
     * Guarda un usuario
     * @param userDto Usuario
     * @return Resultado de la operación
     */
    Mono<UserDTO> save(UserDTO userDto);
    
    /**
     * Elimina un usuario
     * @param userDto usuario a eliminar
     * @return
     */
    Mono<Void> delete(UserDTO userDto);
}
