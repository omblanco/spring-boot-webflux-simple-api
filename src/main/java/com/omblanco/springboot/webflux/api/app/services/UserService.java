package com.omblanco.springboot.webflux.api.app.services;

import com.omblanco.springboot.webflux.api.app.web.dto.UserDTO;

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
