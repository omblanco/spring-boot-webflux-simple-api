package com.omblanco.springboot.webflux.api.commons.services;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.omblanco.springboot.webflux.api.commons.annotation.loggable.Loggable;
import com.omblanco.springboot.webflux.api.commons.annotation.traceable.Traceable;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementación abstracta del servicio genérico
 * para repositorios reactivos de mongo
 * @author oscar.martinezblanco
 *
 * @param <D> Clase DTO
 * @param <E> Clase Entity
 * @param <R> Clase Repository
 * @param <K> Tipo de la clave 
 */
@Traceable
@Loggable
@AllArgsConstructor
public abstract class CommonReactiveServiceImpl <D, E, R extends ReactiveMongoRepository<E, String>, K> implements CommonService<D, E, K>{

    protected R repository;
    
    @Override
    public Flux<D> findAll() {
        
        return repository.
                findAll()
                .map(this::convertToDto);
    }

    @Override
    public Mono<D> findById(K id) {
        return repository.findById(id.toString()).map(this::convertToDto);
    }

    @Override
    public Mono<D> save(D dto) {
        return repository.save(convertToEntity(dto)).map(this::convertToDto);
    }

    @Override
    public Mono<Void> delete(D dto) {
        return repository.delete(convertToEntity(dto)).flatMap(result -> Mono.empty());
    }

    /**
     * Conversión de Entidad a DTO
     * @param entity Entidad
     * @return DTO
     */
    protected abstract D convertToDto(E entity);
    
    /**
     * Transforma una página de entidades a página de dtos
     * @param entityPage Página de entidades
     * @return Página de dtos
     */
    protected abstract Page<D> convertPageToDto(Page<E> entityPage);
    
    /**
     * Transforma un dto a una entidad
     * @param dto DTO
     * @return Entidad
     */
    protected abstract E convertToEntity(D dto);
}
