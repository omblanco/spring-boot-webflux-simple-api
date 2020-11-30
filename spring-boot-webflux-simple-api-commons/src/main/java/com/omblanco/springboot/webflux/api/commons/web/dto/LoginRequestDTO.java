package com.omblanco.springboot.webflux.api.commons.web.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

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
    @NotEmpty
    private String email;
    
    @NotEmpty
    private String password;
}
