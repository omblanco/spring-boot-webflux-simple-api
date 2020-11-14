package com.omblanco.springboot.webflux.api.app.web.dto;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

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
    
    @NotNull
    @Size(min = 3, max = 25)
    private String name;
    
    @NotNull
    @Size(min = 3, max = 50)
    private String surname;
    
    @NotNull
    @Size(min = 3, max = 50)
    @Email
    private String email;
    
    @NotNull
    @Past
    private Date birthdate;
}
