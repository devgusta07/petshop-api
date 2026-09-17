package com.petshop.petshop_api.infra;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class RestError {

    private HttpStatus status;
    private String message;

}
