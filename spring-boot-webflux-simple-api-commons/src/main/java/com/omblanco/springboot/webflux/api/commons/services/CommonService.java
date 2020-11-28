package com.omblanco.springboot.webflux.api.commons.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz del servicio genérico
 * @author oscar.martinezblanco
 *
 * @param <D> Clase DTO
 * @param <E> Clase Entity
 * @param <K> Clase del ID
 */
public interface CommonService<D, E, K> {

    /**
     * Recupera un listado con todos los dtos
     * @return Flux de dtos
     */
    Flux<D> findAll();
    
    /**
     * Busca un dto por la clave
     * @param id Clave
     * @return Dto
     */
    Mono<D> findById(K id);
    
    /**
     * Guarda un dto
     * @param dto dto
     * @return Dto resultado de la operación
     */
    Mono<D> save(D dto);
    
    /**
     * Elimina un dto
     * @param dto dto a eliminar
     * @return Resultado de la operación
     */
    Mono<Void> delete(D dto);
}
