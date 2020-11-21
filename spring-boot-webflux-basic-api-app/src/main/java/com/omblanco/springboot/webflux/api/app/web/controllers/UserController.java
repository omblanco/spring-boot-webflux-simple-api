package com.omblanco.springboot.webflux.api.app.web.controllers;

import static com.omblanco.springboot.webflux.api.app.utils.BaseApiConstants.FORWARD_SLASH;
import static com.omblanco.springboot.webflux.api.app.utils.BaseApiConstants.ID_PARAM_URL;
import static com.omblanco.springboot.webflux.api.app.utils.BaseApiConstants.USER_BASE_URL_V1;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.omblanco.springboot.webflux.api.app.services.UserService;
import com.omblanco.springboot.webflux.api.app.web.dto.UserDTO;
import com.omblanco.springboot.webflux.api.app.web.dto.UserFilterDTO;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador para los usuarios
 * 
 * @author oscar.martinezblanco
 *
 */
@AllArgsConstructor
@Controller
@RequestMapping(USER_BASE_URL_V1)
public class UserController {

    private UserService userService;

    /**
     * Recupera todos los usuarios
     * 
     * @return Lista de usuarios
     */
    @GetMapping
    @ResponseBody
    public Mono<ResponseEntity<Flux<UserDTO>>> findAll() {
        return Mono.just(ResponseEntity.ok().contentType(APPLICATION_JSON).body(userService.findAll()));
    }
    
    @GetMapping(params = {"page", "size"})
    @ResponseBody
    public Mono<ResponseEntity<Mono<Page<UserDTO>>>> findByFilter(UserFilterDTO filter,
            @SortDefault(sort = "id", direction = Sort.Direction.DESC) @PageableDefault(value = 10) Pageable pageable) {
        return Mono.just(ResponseEntity.ok().contentType(APPLICATION_JSON).body(userService.findByFilter(filter, pageable)));
    }    

    /**
     * Recupera un usuario por clave primaria
     * 
     * @param id Clave primaria
     * @return Usuario
     */
    @GetMapping(ID_PARAM_URL)
    @ResponseBody
    public Mono<ResponseEntity<UserDTO>> get(@PathVariable Long id) {
        return userService.findById(id).map(p -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(p))
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
    public Mono<ResponseEntity<UserDTO>> create(@RequestBody @Valid Mono<UserDTO> monoUser) {
        return monoUser.flatMap(user -> {
            return userService.save(user).map(userDb -> {
                return ResponseEntity
                        .created(URI.create(USER_BASE_URL_V1.concat(FORWARD_SLASH).concat(userDb.getId().toString())))
                        .contentType(APPLICATION_JSON).body(userDb);
            });
        });
    }

    /**
     * Actualiza un usuario
     * 
     * @param user Usuario a actualizar
     * @param id   Clave primaria del usuario a actualizar
     * @return Resultado de la actualización
     */
    @PutMapping(ID_PARAM_URL)
    @ResponseBody
    public Mono<ResponseEntity<UserDTO>> update(@PathVariable Long id, @Valid @RequestBody UserDTO user) {
        return userService.findById(id).flatMap(userDb -> {

            userDb.setBirthdate(user.getBirthdate());
            userDb.setEmail(user.getEmail());
            userDb.setName(user.getName());
            userDb.setSurname(user.getSurname());

            return userService.save(userDb);
        }).map(userDb -> ResponseEntity
                .created(URI.create(USER_BASE_URL_V1.concat(FORWARD_SLASH).concat(userDb.getId().toString())))
                .contentType(APPLICATION_JSON)
                .body(userDb))
        .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un usuario
     * 
     * @param id Clave primaria del usuario a eliminar
     * @return Resultado de la operación
     */
    @DeleteMapping(ID_PARAM_URL)
    public Mono<ResponseEntity<Void>> eliminar(@PathVariable Long id) {
        return userService.findById(id).flatMap(userDb -> {
            return userService.delete(userDb).then(Mono.just(new ResponseEntity<Void>(NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(NOT_FOUND));
    }
    
}
