package com.omblanco.springboot.webflux.api.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Clase de configuración para el componente de encriptado y desencriptado de contraseñas
 * @author oscar.martinezblanco
 *
 */
@Configuration
public class BCryptPasswordConfig {
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
