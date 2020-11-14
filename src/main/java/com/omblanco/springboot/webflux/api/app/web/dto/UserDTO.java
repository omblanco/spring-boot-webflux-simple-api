package com.omblanco.springboot.webflux.api.app.web.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Clase de un usuario de la aplicación para la capa web
 * @author oscar.martinezblanco
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {

    private Long id;
    
    private String name;
    
    private String surname;
    
    private String email;
    
    private Date birthdate;
}
