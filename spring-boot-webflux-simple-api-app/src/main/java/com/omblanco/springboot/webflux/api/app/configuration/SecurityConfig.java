package com.omblanco.springboot.webflux.api.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertyResolver;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.omblanco.springboot.webflux.api.commons.security.AuthenticationManager;
import com.omblanco.springboot.webflux.api.commons.security.SecurityContextRepository;
import com.omblanco.springboot.webflux.api.commons.security.TokenProvider;

/**
 * Clase de configuración con los beans necesarios para la autenticación
 * @author oscar.martinezblanco
 *
 */
@Configuration
public class SecurityConfig {

    @Bean
    AuthenticationManager authenticationManager(TokenProvider tokenProvider) {
        return new AuthenticationManager(tokenProvider);
    }
    
    @Bean
    TokenProvider tokenProvider(PropertyResolver propertyResolver) {
        return new TokenProvider(propertyResolver);
    }
    
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    SecurityContextRepository securityContextRepository(AuthenticationManager authenticationManager) {
        return new SecurityContextRepository(authenticationManager);
    }
}
