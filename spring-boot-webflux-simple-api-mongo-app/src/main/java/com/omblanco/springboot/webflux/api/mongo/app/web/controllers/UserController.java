package com.omblanco.springboot.webflux.api.mongo.app.web.controllers;

import static com.omblanco.springboot.webflux.api.commons.utils.BaseApiConstants.USER_BASE_URL_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.omblanco.springboot.webflux.api.commons.web.controllers.CommonController;
import com.omblanco.springboot.webflux.api.commons.web.dto.UserFilterDTO;
import com.omblanco.springboot.webflux.api.mongo.app.model.entity.User;
import com.omblanco.springboot.webflux.api.mongo.app.services.UserService;
import com.omblanco.springboot.webflux.api.mongo.app.web.dtos.UserDTO;

import lombok.Builder;
import reactor.core.CorePublisher;
import reactor.core.publisher.Mono;

/**
 * Controlador para los usuarios
 * 
 * @author oscar.martinezblanco
 *
 */
@Controller
@RequestMapping(USER_BASE_URL_V1)
public class UserController extends CommonController<UserDTO, User, UserService, String> {
    
    @Builder
    public UserController(UserService service) {
        super(service);
    }
    
    /**
     * Método que recupera Usuarios paginados
     * @param filter Filtro de búsqueda
     * @param pageable Paginación y ordenación
     * @return Página de usuarios
     */
    @GetMapping
    @ResponseBody
    public Mono<ResponseEntity<CorePublisher<?>>> findByFilter(UserFilterDTO filter,
            @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Long size, Long page) {
        
        if (size == null & page == null) {
            return super.findAll();
        }
        
        return Mono.just(ResponseEntity.ok().contentType(APPLICATION_JSON).body(service.findByFilter(filter, pageable)));
    }

    @Override
    protected String getBaseUrl() {
        return USER_BASE_URL_V1;
    }

    @Override
    protected void updateDtoToSave(UserDTO requestDto, UserDTO dbDto) {
        dbDto.setBirthdate(requestDto.getBirthdate());
        dbDto.setEmail(requestDto.getEmail());
        dbDto.setName(requestDto.getName());
        dbDto.setSurname(requestDto.getSurname());
    }
}
