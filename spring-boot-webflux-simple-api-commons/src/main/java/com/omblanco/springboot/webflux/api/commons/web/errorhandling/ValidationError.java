package com.omblanco.springboot.webflux.api.commons.web.errorhandling;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ValidationError implements Serializable {

    private static final long serialVersionUID = 1L;
    private String path;
    private String code;
    private String message;

    @JsonCreator
    public ValidationError(String path, String code, String message) {
        this.path = path;
        this.code = code;
        this.message = message;
    }

}