package com.omblanco.springboot.webflux.api.app.web.controllers;

import static com.omblanco.springboot.webflux.api.commons.utils.BaseApiConstants.USER_BASE_URL_V2;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omblanco.springboot.webflux.api.app.model.entity.User;
import com.omblanco.springboot.webflux.api.app.services.UserService;
import com.omblanco.springboot.webflux.api.app.web.dto.UserDTO;
import com.omblanco.springboot.webflux.api.commons.web.controllers.CommonController;
import com.omblanco.springboot.webflux.api.commons.web.dto.UserFilterDTO;

import lombok.Builder;
import reactor.core.CorePublisher;
import reactor.core.publisher.Mono;

/**
 * RestController para los usuarios
 * En este caso la implementación es igual al UserController
 * Al usar la anotación @RestController este controlador sólo puede devolver objetos json
 * por no tanto no puede cargar vistas html a diferencia del UserController
 * No necesita de la anotación @ResponseBody al devolver los resultados
 * 
 * @author oscar.martinezblanco
 *
 */
@RestController
@RequestMapping(USER_BASE_URL_V2)
public class UserRestController extends CommonController<UserDTO, User, UserService, Long> {

    @Builder
    public UserRestController(UserService service) {
        super(service);
    }

    /**
     * Método que recupera Usuarios paginados
     * @param filter Filtro de búsqueda
     * @param pageable Paginación y ordenación
     * @return Página de usuarios
     */
    @GetMapping
    public Mono<ResponseEntity<CorePublisher<?>>> findByFilter(UserFilterDTO filter,
            @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Long size, Long page) {
        
        if (size == null & page == null) {
            return super.findAll();
        }
        
        return Mono.just(ResponseEntity.ok().contentType(APPLICATION_JSON).body(service.findByFilter(filter, pageable)));
    }

    @Override
    protected String getBaseUrl() {
        return USER_BASE_URL_V2;
    }

    @Override
    protected void updateDtoToSave(UserDTO requestDto, UserDTO dbDto) {
        dbDto.setBirthdate(requestDto.getBirthdate());
        dbDto.setEmail(requestDto.getEmail());
        dbDto.setName(requestDto.getName());
        dbDto.setSurname(requestDto.getSurname());
    }
}
