package com.omblanco.springboot.webflux.api.app.web.errorhandling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import reactor.core.publisher.Mono;

/**
 * Handler que captura las excepciones de las peticiones con modelos anotados con @Valid
 * @author oscar.martinezblanco
 *
 */
// see: https://github.com/hantsy/angular-spring-reactive-sample/blob/master/server/src/main/java/com/example/demo/RestExceptionHandler.java
// see: https://stackoverflow.com/questions/47631243/spring-5-reactive-webexceptionhandler-is-not-getting-called
// and https://docs.spring.io/spring-boot/docs/2.0.0.M7/reference/html/boot-features-developing-web-applications.html#boot-features-webflux-error-handling
// and https://stackoverflow.com/questions/48047645/how-to-write-messages-to-http-body-in-spring-webflux-webexceptionhandlder/48057896#48057896
@Component
@Order(-2)
@RequiredArgsConstructor
public class RestValidationExceptionHandler implements WebExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestValidationExceptionHandler.class);
    
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof WebExchangeBindException) {
            var webExchangeBindException = (WebExchangeBindException) ex;

            LOG.debug("Excepción de validación", ex);
            
            var errors = new Errors(HttpStatus.UNPROCESSABLE_ENTITY.name(), 
                    webExchangeBindException.getReason().concat(": ")
                    .concat(webExchangeBindException.getObjectName().replace("Mono", "")));
            
            webExchangeBindException.getFieldErrors().forEach(e -> 
                errors.add(e.getField(), e.getCode(), e.getDefaultMessage()));

            try {
                exchange.getResponse().setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                var db = new DefaultDataBufferFactory().wrap(objectMapper.writeValueAsBytes(errors));
                return exchange.getResponse().writeWith(Mono.just(db));
            } catch (JsonProcessingException e) {
                LOG.error("Error procesando json", e);
                return Mono.empty();
            }
        }
        return Mono.error(ex);
    }
}

@Getter
@ToString
class Errors implements Serializable {
    private static final long serialVersionUID = -646517201289834673L;
    private String code;
    private String message;
    private List<Error> errors = new ArrayList<>();

    @JsonCreator
    Errors(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void add(String path, String code, String message) {
        this.errors.add(new Error(path, code, message));
    }
}

@Getter
@ToString
class Error implements Serializable {

    private static final long serialVersionUID = 1L;
    private String path;
    private String code;
    private String message;

    @JsonCreator
    Error(String path, String code, String message) {
        this.path = path;
        this.code = code;
        this.message = message;
    }

}
