package com.omblanco.springboot.webflux.api.mongo.app.web.handlers;

import static com.omblanco.springboot.webflux.api.commons.utils.BaseApiConstants.FORWARD_SLASH;
import static com.omblanco.springboot.webflux.api.commons.utils.BaseApiConstants.ID_PARAM_NAME;
import static com.omblanco.springboot.webflux.api.commons.utils.BaseApiConstants.USER_BASE_URL_V3;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.omblanco.springboot.webflux.api.commons.annotation.loggable.Loggable;
import com.omblanco.springboot.webflux.api.commons.web.dto.UserFilterDTO;
import com.omblanco.springboot.webflux.api.commons.web.handler.CommonHandler;
import com.omblanco.springboot.webflux.api.mongo.app.services.UserService;
import com.omblanco.springboot.webflux.api.mongo.app.web.dtos.UserDTO;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Alternativa al UserController y UserRestController 
 * con functional endpoints de Spring WebFlux
 * @author oscar.martinezblanco
 *
 */
@Loggable
@AllArgsConstructor
@Component
public class UserHandler extends CommonHandler {

    protected static final String NAME_PARAM_NAME = "name";
    protected static final String SURNAME_PARAM_NAME = "surname";
    protected static final String EMAIL_PARAM_NAME = "email";
    
    private static final String VALIDATION_MESSAGE = "Validation failure: userDTO";
    
    private UserService userService;
    
    private Validator validator;
    
    public Mono<ServerResponse> findAll(ServerRequest request) {
        
        Boolean validSize = validateIsPresentAndNotEmptyParam(request, SIZE_PARAM_NAME);
        Boolean validPage = validateIsPresentAndNotEmptyParam(request, PAGE_PARAM_NAME);
        
        if (validSize && validPage) {
            UserFilterDTO filter = new UserFilterDTO();
            filter.setName(getParamValue(request, NAME_PARAM_NAME));
            filter.setSurname(getParamValue(request, SURNAME_PARAM_NAME));
            filter.setEmail(getParamValue(request, EMAIL_PARAM_NAME));
            
            Pageable pageable = getPageableFromRequest(request);
            
            return ServerResponse.ok()
                    .contentType(APPLICATION_JSON)
                    .body(userService.findByFilter(filter, pageable), Page.class);
        } else {
            return ServerResponse.ok()
                    .contentType(APPLICATION_JSON)
                    .body(userService.findAll(), UserDTO.class);
        }
    }
    
    @Deprecated
    public Mono<ServerResponse> findByFilter(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(userService.findAll(), UserDTO.class);
    }
    
    public Mono<ServerResponse> get(ServerRequest request) {
        String id = request.pathVariable(ID_PARAM_NAME);
        
        return userService.findById(id)
                .flatMap(p -> ServerResponse
                        .ok()
                        .contentType(APPLICATION_JSON)
                        .body(fromValue(p)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    
    public Mono<ServerResponse> create(ServerRequest request){
        Mono<UserDTO> user = request.bodyToMono(UserDTO.class);
        
        return user.flatMap(userDto -> {
            Errors errors = new BeanPropertyBindingResult(userDto, UserDTO.class.getName());
            
            validator.validate(userDto, errors);
            
            if(errors.hasErrors()) {
                return validationErrorsResponse(errors, VALIDATION_MESSAGE);
            } else {
                userDto.setId(null);
                return userService.save(userDto).flatMap(userDB -> 
                    ServerResponse.created(URI.create(USER_BASE_URL_V3.concat(FORWARD_SLASH).concat(userDB.getId())))
                    .contentType(APPLICATION_JSON)
                    .body(fromValue(userDB)));
            }
        });
    }
    
    public Mono<ServerResponse> update(ServerRequest request) {
        
        Mono<UserDTO> userMonoDto = request.bodyToMono(UserDTO.class);
        String id = request.pathVariable(ID_PARAM_NAME);
        
        return userMonoDto.flatMap(userDto -> {
            Errors errors = new BeanPropertyBindingResult(userDto, UserDTO.class.getName());
            validator.validate(userDto, errors);
            if(errors.hasErrors()) {
                return validationErrorsResponse(errors, VALIDATION_MESSAGE);
            } else {
                Mono<UserDTO> userMonoDB = userService.findById(id);
                return userMonoDB.map(db -> {
                    db.setName(userDto.getName());
                    db.setSurname(userDto.getSurname());
                    db.setEmail(userDto.getEmail());
                    db.setBirthdate(userDto.getBirthdate());
                    
                    return db;
                }).flatMap(user -> {
                    return userService.save(user).flatMap(updatedUser -> 
                    
                    ServerResponse.created(URI.create(USER_BASE_URL_V3.concat(FORWARD_SLASH).concat(updatedUser.getId())))
                        .contentType(APPLICATION_JSON)
                        .body(fromValue(updatedUser)));
                });
            }
        });
    }
    
    public Mono<ServerResponse> delete(ServerRequest request) {
        
        String id = request.pathVariable(ID_PARAM_NAME);
        Mono<UserDTO> userDB = userService.findById(id);
        
        return userDB.flatMap(p -> userService.delete(p)
                .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}    

