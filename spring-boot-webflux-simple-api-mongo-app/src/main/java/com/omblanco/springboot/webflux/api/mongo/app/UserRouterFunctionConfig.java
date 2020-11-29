package com.omblanco.springboot.webflux.api.mongo.app;

import static com.omblanco.springboot.webflux.api.mongo.app.utils.BaseApiConstants.AUTH_URL_V1;
import static com.omblanco.springboot.webflux.api.mongo.app.utils.BaseApiConstants.ID_PARAM_URL;
import static com.omblanco.springboot.webflux.api.mongo.app.utils.BaseApiConstants.USER_BASE_URL_V3;
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

import com.omblanco.springboot.webflux.api.mongo.app.web.handlers.AuthHandler;
import com.omblanco.springboot.webflux.api.mongo.app.web.handlers.UserHandler;

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
        return route(GET(USER_BASE_URL_V3), userHandler::findAll)
                .andRoute(GET(USER_BASE_URL_V3.concat(ID_PARAM_URL)), userHandler::get)
                .andRoute(POST(USER_BASE_URL_V3).and(contentType(APPLICATION_JSON)), userHandler::create)
                .andRoute(PUT(USER_BASE_URL_V3.concat(ID_PARAM_URL)).and(contentType(APPLICATION_JSON)), userHandler::update)
                .andRoute(DELETE(USER_BASE_URL_V3.concat(ID_PARAM_URL)), userHandler::delete);
    }
    
    @Bean
    public RouterFunction<ServerResponse> authRoutes() {
        return RouterFunctions
                .route(POST(AUTH_URL_V1).and(accept(APPLICATION_JSON)), authHandler::login);
    }
}
