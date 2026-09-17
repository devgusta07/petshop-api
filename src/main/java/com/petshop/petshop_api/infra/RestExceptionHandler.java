package com.petshop.petshop_api.infra;

import com.petshop.petshop_api.exception.RecursoEmUsoException;
import com.petshop.petshop_api.exception.RecursoJaExistenteException;
import com.petshop.petshop_api.exception.RecursoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestError recursoNaoEncontrado(RecursoNaoEncontradoException e) {
        return new RestError(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(RecursoJaExistenteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public RestError recursoJaExistente(RecursoJaExistenteException e) {
        return new RestError(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(RecursoEmUsoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public RestError recursoEmUso(RecursoEmUsoException e) {
        return new RestError(HttpStatus.CONFLICT, e.getMessage());
    }

}
