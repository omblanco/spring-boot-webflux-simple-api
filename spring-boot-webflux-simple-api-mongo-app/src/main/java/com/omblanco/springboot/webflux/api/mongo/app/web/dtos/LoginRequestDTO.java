package com.omblanco.springboot.webflux.api.mongo.app.web.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Clase DTO con la petición de login
 * @author oscar.martinezblanco
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequestDTO {

    @Email
    @NotNull
    private String email;
    
    @NotNull
    private String password;
}
