package com.omblanco.springboot.webflux.api.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * Filtro para habilitar CORS
 * @author oscar.martinezblanco
 *
 */

@Profile("security")
@Configuration
@EnableWebFlux
public class CORSFilter implements WebFluxConfigurer {

    private static final String ASTERISK = "*";
    private static final String ALL_URL_PATTERN = "/**";
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(ALL_URL_PATTERN)
            .allowCredentials(true)
            .allowedOrigins(ASTERISK)
            .allowedHeaders(ASTERISK)
            .allowedMethods(ASTERISK)
             .exposedHeaders(HttpHeaders.SET_COOKIE);
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedHeader(ASTERISK);
        corsConfiguration.addAllowedMethod(ASTERISK);
        corsConfiguration.addAllowedOrigin(ASTERISK);
        corsConfiguration.addExposedHeader(HttpHeaders.SET_COOKIE);
        
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration(ALL_URL_PATTERN, corsConfiguration);
        
        return new CorsWebFilter(corsConfigurationSource);
    }
}