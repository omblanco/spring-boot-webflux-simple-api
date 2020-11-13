package com.omblanco.springboot.webflux.api.app.services;

import com.omblanco.springboot.webflux.api.app.model.entity.User;

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
    Flux<User> findAll();
    
    /**
     * Busca un usuario por la clave
     * @param id Clave
     * @return Usuario
     */
    Mono<User> findById(Long id);
    
    /**
     * Guarda un usuario
     * @param user Usuario
     * @return Resultado de la operación
     */
    Mono<User> save(User user);
    
    /**
     * Elimina un usuario
     * @param user usuario a eliminar
     * @return
     */
    Mono<Void> delete(User user);
}
