package com.omblanco.springboot.webflux.api.app;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.omblanco.springboot.webflux.api.app.utils.BaseApiConstants;
import com.omblanco.springboot.webflux.api.app.web.handler.AuthHandler;
import com.omblanco.springboot.webflux.api.app.web.handler.UserHandler;

/**
 * Configuración para los Functional Endpoints de usuario
 * @author oscar.martinezblanco
 *
 */
@Configuration
public class UserRouterFunctionConfig {

    @Autowired
    private UserHandler userHandler; 
    
    @Autowired
    private AuthHandler authHandler;
    
    @Bean
    public RouterFunction<ServerResponse> routes() {
        return route(GET(BaseApiConstants.USER_BASE_URL_V3), userHandler::findAll)
                .andRoute(GET(BaseApiConstants.USER_BASE_URL_V3.concat(BaseApiConstants.ID_PARAM_URL)), userHandler::get)
                .andRoute(POST(BaseApiConstants.USER_BASE_URL_V3).and(contentType(APPLICATION_JSON)), userHandler::create)
                .andRoute(PUT(BaseApiConstants.USER_BASE_URL_V3.concat(BaseApiConstants.ID_PARAM_URL)).and(contentType(APPLICATION_JSON)), userHandler::update)
                .andRoute(DELETE(BaseApiConstants.USER_BASE_URL_V3.concat(BaseApiConstants.ID_PARAM_URL)), userHandler::delete);
    }
    
    @Bean
    public RouterFunction<ServerResponse> authRoutes() {
        return RouterFunctions
                .route(POST("/auth/login").and(accept(APPLICATION_JSON)), authHandler::login);
    }
}
