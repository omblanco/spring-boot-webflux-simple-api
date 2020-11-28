package com.omblanco.springboot.webflux.api.commons.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommonDTO<K> {

    private K id;
}
