package com.omblanco.springboot.webflux.api.mongo.app.sercurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Clase de configuración de Spring Security
 * @author oscar.martinezblanco
 *
 */
@AllArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig{

    private AuthenticationManager authenticationManager;

    private SecurityContextRepository securityContextRepository;

    @Profile("security")
    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        String[] patterns = new String[] {"/auth/**", "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/webjars/**"};
        
        return http.cors().disable()
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
                    swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                })).accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
                    swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                })).and()
                .csrf().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(patterns).permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .anyExchange().authenticated()
                .and()
                .build();
    }
    
    @Profile("!security")
    @Bean
    SecurityWebFilterChain noSecuritySpringWebFilterChain(ServerHttpSecurity http) {
        return http.csrf().disable().authorizeExchange()
                    .anyExchange().permitAll()
                    .and().build();
    }
}
