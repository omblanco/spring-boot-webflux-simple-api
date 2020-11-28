package com.omblanco.springboot.webflux.api.mongo.app.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserFilterDTO {
    
    private String name;
    
    private String surname;
    
    private String email;
}
