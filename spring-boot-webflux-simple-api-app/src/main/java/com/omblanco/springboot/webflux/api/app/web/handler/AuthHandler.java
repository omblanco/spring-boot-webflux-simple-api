package com.omblanco.springboot.webflux.api.app.web.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.omblanco.springboot.webflux.api.app.security.TokenProvider;
import com.omblanco.springboot.webflux.api.app.services.UserService;
import com.omblanco.springboot.webflux.api.app.web.dto.LoginRequestDTO;
import com.omblanco.springboot.webflux.api.app.web.dto.LoginResponseDTO;
import com.omblanco.springboot.webflux.api.commons.annotation.loggable.Loggable;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Handler con el Functional Endpoint para la 
 * autenticación de usuarios.
 * @author oscar.martinezblanco
 *
 */
@Loggable
@AllArgsConstructor
@Component
public class AuthHandler {

    private BCryptPasswordEncoder passwordEncoder;

    private TokenProvider tokenProvider;

    private UserService userService;

    public Mono<ServerResponse> login(ServerRequest request) {
        
        Mono<LoginRequestDTO> loginRequest = request.bodyToMono(LoginRequestDTO.class);
        
        return loginRequest.flatMap(login -> userService.findByEmail(login.getEmail())
            .flatMap(user -> {
                if (passwordEncoder.matches(login.getPassword(), user.getPassword())) {
                    return ServerResponse.ok()
                            .contentType(APPLICATION_JSON)
                            .body(BodyInserters.fromValue(new LoginResponseDTO(tokenProvider.generateToken(user))));
                } else {
                    return ServerResponse.status(UNAUTHORIZED).build();
                }
            }).switchIfEmpty(ServerResponse.status(UNAUTHORIZED).build()));
        
    }
}
