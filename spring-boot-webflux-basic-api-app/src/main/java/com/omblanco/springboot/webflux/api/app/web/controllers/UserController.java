package com.omblanco.springboot.webflux.api.app.web.controllers;

import static com.omblanco.springboot.webflux.api.app.utils.BaseApiConstants.USER_BASE_URL_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.omblanco.springboot.webflux.api.app.model.entity.User;
import com.omblanco.springboot.webflux.api.app.services.UserService;
import com.omblanco.springboot.webflux.api.app.web.dto.UserDTO;
import com.omblanco.springboot.webflux.api.app.web.dto.UserFilterDTO;
import com.omblanco.springboot.webflux.api.commons.web.controllers.CommonController;

import lombok.Builder;
import reactor.core.publisher.Mono;

/**
 * Controlador para los usuarios
 * 
 * @author oscar.martinezblanco
 *
 */
@Controller
@RequestMapping(USER_BASE_URL_V1)
public class UserController extends CommonController<UserDTO, User, UserService> {
    
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
    @GetMapping(params = {"page", "size"})
    @ResponseBody
    public Mono<ResponseEntity<Mono<Page<UserDTO>>>> findByFilter(UserFilterDTO filter,
            @SortDefault(sort = "id", direction = Sort.Direction.DESC) @PageableDefault(value = 10) Pageable pageable) {
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