package com.omblanco.springboot.webflux.api.commons.web.dto;

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
