package com.omblanco.springboot.webflux.api.commons.web.controllers;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.omblanco.springboot.webflux.api.commons.annotation.loggable.Loggable;
import com.omblanco.springboot.webflux.api.commons.services.CommonService;
import com.omblanco.springboot.webflux.api.commons.web.dto.CommonDTO;

import lombok.AllArgsConstructor;
import reactor.core.CorePublisher;
import reactor.core.publisher.Mono;

/**
 * Controlador genérico
 * @author oscar.martinezblanco
 *
 * @param <D> Clase DTO
 * @param <F> Clase Filter
 * @param <E> Clase Entity
 * @param <S> Clase del servicio
 * @param <K>
 */
@Loggable
@AllArgsConstructor
public abstract class CommonController <D extends CommonDTO<K>, E, S extends CommonService<D, E, K>, K>{

    protected static final String ID_PARAM_URL = "/{id}";
    
    protected static final String FORWARD_SLASH = "/";
    
    protected S service;
    
    /**
     * Recupera una lista de dtos
     * @return Lista de dtos
     */
    @ResponseBody
    protected Mono<ResponseEntity<CorePublisher<?>>> findAll() {
        return Mono.just(ResponseEntity.ok().contentType(APPLICATION_JSON).body(service.findAll()));
    }
    
    /**
     * Recupera un dto por clave primaria
     * 
     * @param id Clave primaria
     * @return DTO
     */
    @GetMapping(ID_PARAM_URL)
    @ResponseBody
    public Mono<ResponseEntity<D>> get(@PathVariable K id) {
        return service.findById(id).map(d -> ResponseEntity.ok()
                .contentType(APPLICATION_JSON).body(d))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    /**
     * Crea un usuario
     * 
     * @param monoUser Usuario a guardar
     * @return Usuario resultado de la operación
     */
    @PostMapping
    @ResponseBody
    public Mono<ResponseEntity<D>> create(@RequestBody @Valid Mono<D> monoDto) {
        return monoDto.flatMap(dto -> {
            dto.setId(null);
            return service.save(dto).map(dtoDb -> {
                
                return ResponseEntity
                        .created(URI.create(getBaseUrl().concat(FORWARD_SLASH).concat(dtoDb.getId().toString())))
                        .contentType(APPLICATION_JSON).body(dtoDb);
            });
        });
    }
    
    /**
     * Actualiza un dto
     * 
     * @param dto DTO a actualizar
     * @param id   Clave primaria del DTO a actualizar
     * @return Resultado de la actualización
     */
    @PutMapping(ID_PARAM_URL)
    @ResponseBody
    public Mono<ResponseEntity<D>> update(@PathVariable K id, @Valid @RequestBody D dto) {
        return service.findById(id).flatMap(dtoToSave -> {
            
            updateDtoToSave(dto, dtoToSave);

            return service.save(dtoToSave);
        }).map(dtoToSave -> ResponseEntity
                .created(URI.create(getBaseUrl().concat(FORWARD_SLASH).concat(dtoToSave.getId().toString())))
                .contentType(APPLICATION_JSON)
                .body(dtoToSave))
        .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    /**
     * Elimina un dto
     * 
     * @param id Clave primaria del dto a eliminar
     * @return Resultado de la operación
     */
    @DeleteMapping(ID_PARAM_URL)
    public Mono<ResponseEntity<Void>> delete(@PathVariable K id) {
        return service.findById(id).flatMap(dtoDb -> {
            return service.delete(dtoDb).then(Mono.just(new ResponseEntity<Void>(NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(NOT_FOUND));
    }
    
    protected abstract String getBaseUrl();
    
    protected abstract void updateDtoToSave(D requestDto, D dbDto);
}
