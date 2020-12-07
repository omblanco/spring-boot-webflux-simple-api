package com.omblanco.springboot.webflux.api.client;

import com.omblanco.springboot.webflux.api.client.dto.UserDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz del cliente reactivo para consumir la api
 * @author oscar.martinezblanco
 *
 */
public interface ReactiveUsersClient<K> {

    /**
     * Recupera todos los usuarios
     * @return Flux de usuarios
     */
    Flux<UserDTO<K>> getAllUsers();
    
    /**
     * Recuera un usuario por id
     * @param id Id
     * @return Usuario
     */
    Mono<UserDTO<K>> get(K id);
    
    /**
     * Crea un nuevo usuario
     * @param user Usuario
     * @return Usuario creado
     */
    Mono<UserDTO<K>> save(UserDTO<K> user);
    
    /**
     * Actualiza un usuario
     * @param user Usuario
     * @param id Id
     * @return Usuario actualizado
     */
    Mono<UserDTO<K>> update(UserDTO<K> user, K id);
    
    /**
     * Elimina un usuario
     * @param id Id de usuario a eliminar
     * @return Resultado de la operación
     */
    Mono<Void> delete(K id);
}
