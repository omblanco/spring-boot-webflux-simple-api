package com.omblanco.springboot.webflux.api.commons.services;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.omblanco.springboot.webflux.api.commons.annotation.loggable.Loggable;
import com.omblanco.springboot.webflux.api.commons.annotation.traceable.Traceable;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Implementación abstracta del servicio genérico
 * para repositorios no reactivos
 * @author oscar.martinezblanco
 *
 * @param <D> Clase DTO
 * @param <E> Clase Entity
 * @param <R> Clase Repository
 * @param <K>
 */
@Traceable
@Loggable
@AllArgsConstructor
public abstract class CommonServiceImpl <D, E, R extends JpaRepository<E, K>, K> implements CommonService<D, E, K> {

    protected R repository;
    
    @Override
    public Flux<D> findAll() {
        return Flux.defer(() -> Flux.fromIterable(repository.findAll()
                .stream().map(this::convertToDto)
                .collect(Collectors.toList())))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<D> findById(K id) {
        return Mono.defer(() -> Mono.just(repository.findById(id))).flatMap(optional -> {
            if (optional.isPresent()) {
                return Mono.just(convertToDto(optional.get()));
            }
            
            return Mono.empty();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<D> save(D dto) {
        return Mono.defer(() -> Mono.just(convertToDto(repository.save(convertToEntity(dto)))))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> delete(D dto) {
        return Mono.defer(() -> {
            repository.delete(convertToEntity(dto));
            return Mono.empty();
        }).subscribeOn(Schedulers.boundedElastic()).then();
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
